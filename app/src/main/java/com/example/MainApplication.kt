package com.example

import android.app.Application
import com.example.ui.AdmobInterstitial
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Configure Mobile Ads SDK safely
        try {
            if (BuildConfig.DEBUG) {
                val configuration = RequestConfiguration.Builder()
                    .setTestDeviceIds(listOf(AdRequest.DEVICE_ID_EMULATOR))
                    .build()
                MobileAds.setRequestConfiguration(configuration)
            }
            MobileAds.initialize(this) {
                AdmobInterstitial.loadAd(this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
