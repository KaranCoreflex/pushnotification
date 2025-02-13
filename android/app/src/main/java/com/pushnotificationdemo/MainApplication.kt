package com.mycoke360.android.cona.perf

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.load
import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost
import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.soloader.SoLoader
import com.facebook.react.soloader.OpenSourceMergedSoMapping
import com.salesforce.marketingcloud.MarketingCloudConfig
import com.salesforce.marketingcloud.UrlHandler
import com.salesforce.marketingcloud.notifications.NotificationCustomizationOptions
import com.salesforce.marketingcloud.notifications.NotificationManager
import com.salesforce.marketingcloud.notifications.NotificationMessage
import com.salesforce.marketingcloud.sfmcsdk.InitializationStatus
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdk
import com.salesforce.marketingcloud.sfmcsdk.SFMCSdkModuleConfig
import kotlin.random.Random


class MainApplication : Application(), ReactApplication, UrlHandler,
  NotificationManager.NotificationChannelIdProvider, NotificationManager.NotificationLaunchIntentProvider {
  override val reactNativeHost: ReactNativeHost =
    object : DefaultReactNativeHost(this) {
      override fun getPackages(): List<ReactPackage> =
        PackageList(this).packages.apply {
          // Packages that cannot be autolinked yet can be added manually here, for example:
          // add(MyReactNativePackage())
        }

      override fun getJSMainModuleName(): String = "index"

      override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

      override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
      override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED
    }

  override val reactHost: ReactHost
    get() = getDefaultReactHost(applicationContext, reactNativeHost)

  override fun onCreate() {
    super.onCreate()
    SoLoader.init(this, OpenSourceMergedSoMapping)
    if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
      // If you opted-in for the New Architecture, we load the native entry point for this app.
      load()
    }

    SFMCSdk.configure(
      applicationContext,
      SFMCSdkModuleConfig.build {
        pushModuleConfig =
          MarketingCloudConfig.builder()
            .apply {
              //Update these details based on your MC config
              setApplicationId("941a6278-64b3-4c93-96be-f7cea1c2e1fd")
              setAccessToken("D0IFPPvfcFkIgOmA8qXJOEsz")
              setSenderId("362374082564")
              setMarketingCloudServerUrl("https://mc2rqz5lct-2dzm4g053-zzp7h04.device.marketingcloudapis.com/")
              setNotificationCustomizationOptions(NotificationCustomizationOptions.create(R.drawable.ic_notification))
              setUrlHandler(this@MainApplication)
            }
            .build(applicationContext)
      }
    ) { initStatus ->
      when (initStatus.status) {
        InitializationStatus.SUCCESS -> Log.d("SFMC", "SFMC SDK Initialization Successful")
        InitializationStatus.FAILURE -> Log.d("SFMC", "SFMC SDK Initialization Failed")
        else -> Log.d("SFMC", "SFMC SDK Initialization Status: Unknown")
      }
    }
  }

  override fun getNotificationChannelId(context: Context, notificationMessage: NotificationMessage): String {
    Log.d("SFMC", "getNotificationChannelId")
    return NotificationManager.createDefaultNotificationChannel(context)
  }

  override fun handleUrl(context: Context, url: String, urlSource: String): PendingIntent? {
    Log.d("SFMC", "handleUrl")
    return getPendingIntent(context, url)
  }

  override fun getNotificationPendingIntent(context: Context, notificationMessage: NotificationMessage): PendingIntent {
    Log.d("SFMC", "getNotificationPendingIntent")
    return getPendingIntent(context, notificationMessage.url)
  }

  private fun getPendingIntent(context: Context, url: String?): PendingIntent {
    return when {
      url.isNullOrEmpty() -> {
        Log.d("SFMC", "getPendingIntent url.isNullOrEmpty ")
        PendingIntent.getActivity(
          context,
          Random.nextInt(),
          context.packageManager.getLaunchIntentForPackage(context.packageName),
          provideIntentFlags(),
        )
      }

      else -> {
        Log.d("SFMC", "url.isNullOrEmpty else")
      PendingIntent.getActivity(
        context,
        Random.nextInt(),
        Intent(Intent.ACTION_VIEW, Uri.parse(url)),
        provideIntentFlags(),
      )
    }
    }
  }

  private fun provideIntentFlags(): Int {
    Log.d("SFMC", "provideIntentFlags")
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    } else {
      PendingIntent.FLAG_UPDATE_CURRENT
    }
  }
}
