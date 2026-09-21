package com.example.network

import com.example.engine.AstronomicalEngine
import com.example.engine.TrustedDataVerificationService
import com.example.model.CalendarTradition
import com.example.model.CityLocation
import com.example.model.DayPanchanga
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class PanchangNetworkRepository {

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "TeluguPanchangamApp/2.4 (Android; Official Drik Ganita Ephemeris)")
                    .header("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(8, TimeUnit.SECONDS)
            .readTimeout(8, TimeUnit.SECONDS)
            .build()
    }

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    private val apiService: PanchangApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://telugu.panchangam.org/") // Official source base endpoint
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PanchangApiService::class.java)
    }

    /**
     * Fetches Panchangam for a given date and location.
     * Attempts network fetch via Retrofit/OkHttp, and falls back to astronomical ephemeris if offline or remote unavailable.
     */
    suspend fun getPanchangData(
        date: LocalDate,
        location: CityLocation,
        tradition: CalendarTradition
    ): DayPanchanga {
        // Fallback calculation via Drik Ganita engine
        val localPanchang = AstronomicalEngine.calculatePanchanga(date, location, tradition)
        
        return try {
            val formattedDate = date.toString()
            val response = apiService.getPanchangForDate(
                dateStr = formattedDate,
                lat = location.latitude,
                lon = location.longitude,
                timezone = location.timezoneId,
                tradition = tradition.name
            )

            if (response.isSuccessful && response.body() != null) {
                val remote = response.body()!!
                // Integrate remote data into DayPanchanga if fields match
                val verifiedPanchang = localPanchang.copy(
                    hinduMasa = remote.masa ?: localPanchang.hinduMasa
                )
                TrustedDataVerificationService.verifyAndAnnotate(verifiedPanchang, date, location)
                verifiedPanchang
            } else {
                TrustedDataVerificationService.verifyAndAnnotate(localPanchang, date, location)
                localPanchang
            }
        } catch (e: Exception) {
            // Network failure / offline mode -> return verified astronomical ephemeris calculation
            TrustedDataVerificationService.verifyAndAnnotate(localPanchang, date, location)
            localPanchang
        }
    }
}
