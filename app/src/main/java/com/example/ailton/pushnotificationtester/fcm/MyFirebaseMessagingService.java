package com.example.ailton.pushnotificationtester.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.ailton.pushnotificationtester.MainActivity;
import com.example.ailton.pushnotificationtester.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        String title = "";
        String body = "";

        Map<String, String> data = remoteMessage.getData();
        if (data != null ) {
            title = data.get("title");
            body = data.get("body");
        }
        // Check if message contains a notification payload.
        else if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            body = remoteMessage.getNotification().getBody();
            title = remoteMessage.getNotification().getTitle();

            //If has no title, use the default text for title
            //Probably its a message incoming from firebase console
            if (title == null || title.length() == 0) {
                title = getString(R.string.notification);
            }
        }

        //shows notification
        sendNotification(title, body);
    }

    /**
     * Show a notification and open the app when user clicks on it.
     * @param messageTitle Notification title
     * @param messageBody Message to show
     */
    private void sendNotification(String messageTitle, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.putExtras(b);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,  PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_default_notification)
                .setContentTitle(messageTitle)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setDefaults(Notification.DEFAULT_ALL) //vibration
                .setPriority(NotificationCompat.PRIORITY_HIGH) //High priority to becomes heads up notification
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));
        NotificationManager notificationManager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
