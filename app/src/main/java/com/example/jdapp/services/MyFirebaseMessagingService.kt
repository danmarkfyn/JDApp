package com.example.jdapp.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.jdapp.DisplayHospitalsActivity
import com.example.jdapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * This class is used to getting the notifications from the Firebase Messaging Service if any.
 * It translate the message into the notification which is displayed on user device
 */

class MyFirebaseMessagingService : FirebaseMessagingService() {

    val TAG = "MyFirebaseInstantMes"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d(TAG, "From: ${remoteMessage?.from}")

        remoteMessage?.data?.isEmpty()?.let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            if (!remoteMessage.data.isNullOrEmpty()) {
                showNotification(
                    remoteMessage.notification?.title,
                    remoteMessage.notification?.body
                )
            }
        }

        remoteMessage?.notification?.let {
            showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
        }
    }
    //Used to show notification when occurs
    private fun showNotification(title: String?, body: String?) {
        //When user click on the notification, he will be redirected into main menu
        val intent = Intent(this, DisplayHospitalsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        //Building notification for FirebaseMessagingService
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
}