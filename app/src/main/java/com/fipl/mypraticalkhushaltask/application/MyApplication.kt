package com.fipl.mypraticalkhushaltask.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.facebook.drawee.backends.pipeline.Fresco
import com.fipl.mypraticalkhushaltask.R
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics


class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
        fun get(): MyApplication? {
            return instance
        }

    }
    /*companion object {
        lateinit var instance: MyApplication
                        private set
    }*/

    override fun onCreate() {
        super.onCreate()
        instance = this
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        FirebaseApp.initializeApp(this)
        MultiDex.install(this)
        Fresco.initialize(this)
        createChannel()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

    }

    fun createChannel(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationManager = getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            var channelId=instance.resources.getString(R.string.app_name)
            // Create the NotificationChannel
            val name = instance.resources.getString(R.string.app_name)
            val descriptionText = instance.resources.getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val existingChannel = notificationManager.getNotificationChannel(channelId)

//it will delete existing channel if it exists
            if (existingChannel != null) {
                notificationManager.deleteNotificationChannel(existingChannel.id)
            }
            var defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val mChannel = NotificationChannel(channelId, name, importance)
            mChannel.description = descriptionText

//            var defaultSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + instance.packageName + "/" + R.raw.order_receive)

            mChannel.setShowBadge(true)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            mChannel.setSound(defaultSoundUri, audioAttributes)
            mChannel.enableLights(true)
            mChannel.enableVibration(true)
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}