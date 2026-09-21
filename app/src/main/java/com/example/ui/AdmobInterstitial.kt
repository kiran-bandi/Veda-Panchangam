package com.example.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * Helper object for loading and presenting AdMob Interstitial Ads.
 * Uses Google Test Ad Unit ID by default for safe layout testing.
 * Production Ad Unit ID: ca-app-pub-5013184979910434/6826280649
 */
object AdmobInterstitial {
    const val DEMO_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
    const val PRODUCTION_AD_UNIT_ID = "ca-app-pub-5013184979910434/6826280649"

    // Set to true to switch from Demo ID to Production ID when deploying
    var useProductionAdUnit: Boolean = true

    private val currentAdUnitId: String
        get() = if (useProductionAdUnit) PRODUCTION_AD_UNIT_ID else DEMO_AD_UNIT_ID

    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false

    fun loadAd(context: Context, adUnitId: String = currentAdUnitId) {
        if (interstitialAd != null || isLoading) return
        isLoading = true
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context.applicationContext,
            adUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoading = false
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    interstitialAd = null
                    isLoading = false
                }
            }
        )
    }

    fun showAd(context: Context, onAdClosed: (() -> Unit)? = null) {
        val activity = context.findActivity()
        val ad = interstitialAd
        if (ad != null && activity != null) {
            interstitialAd = null
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    loadAd(context)
                    onAdClosed?.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    loadAd(context)
                    onAdClosed?.invoke()
                }
            }
            ad.show(activity)
        } else {
            loadAd(context)
            onAdClosed?.invoke()
        }
    }

    private fun Context.findActivity(): Activity? {
        var currentContext = this
        while (currentContext is ContextWrapper) {
            if (currentContext is Activity) {
                return currentContext
            }
            currentContext = currentContext.baseContext
        }
        return null
    }
}
