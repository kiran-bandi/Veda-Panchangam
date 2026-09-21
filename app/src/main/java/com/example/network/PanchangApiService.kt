package com.example.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PanchangApiService {

    @GET("api/v1/panchang")
    suspend fun getPanchangForDate(
        @Query("date") dateStr: String, // YYYY-MM-DD
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("tz") timezone: String,
        @Query("tradition") tradition: String = "TELUGU"
    ): Response<RemotePanchangResponse>

    @GET("api/v1/today")
    suspend fun getTodayPanchangSummary(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("tz") timezone: String
    ): Response<RemotePanchangResponse>
}
