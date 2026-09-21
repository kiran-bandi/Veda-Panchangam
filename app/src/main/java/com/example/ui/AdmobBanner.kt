package com.example.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

object AdmobBannerConfig {
    const val DEMO_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    const val PRODUCTION_BANNER_AD_UNIT_ID = "ca-app-pub-5013184979910434/3918238245"
    var useProductionAdUnit: Boolean = true

    val currentAdUnitId: String
        get() = if (useProductionAdUnit) PRODUCTION_BANNER_AD_UNIT_ID else DEMO_BANNER_AD_UNIT_ID
}

/**
 * Reusable Jetpack Compose AdMob Banner Composable.
 * Uses AndroidView to instantiate and host Google Mobile Ads AdView.
 */
@Composable
fun AdmobBanner(
    modifier: Modifier = Modifier,
    adUnitId: String = AdmobBannerConfig.currentAdUnitId
) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                setAdUnitId(adUnitId)
                adListener = object : AdListener() {
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        // Handled safely
                    }
                }
                loadAd(AdRequest.Builder().build())
            }
        },
        update = { adView ->
            // Clean layout modifications during recomposition
        }
    )
}
