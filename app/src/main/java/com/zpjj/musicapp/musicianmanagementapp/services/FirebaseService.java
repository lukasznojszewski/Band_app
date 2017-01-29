package com.zpjj.musicapp.musicianmanagementapp.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.activities.MainActivity;
import com.zpjj.musicapp.musicianmanagementapp.activities.auth.AuthActivity;
import com.zpjj.musicapp.musicianmanagementapp.models.Band;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by daniel on 23.01.17.
 */

public class FirebaseService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";


    public FirebaseService() {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.d(TAG, "Notification type : " + remoteMessage.getData().get("type"));
        showNotification(remoteMessage, getTypeFromString(remoteMessage.getData().get("type")));
    }

    private void showNotification(RemoteMessage message, ENotifyType type) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_light) // notification icon
                .setContentTitle(message.getNotification().getTitle()) // title for notification
                .setContentText(message.getNotification().getBody()) // message for notification
                .setSound(soundUri)
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("NOTIFY_TYPE", type.toString());
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }


    enum ENotifyType {
        JOIN_BAND_REQUEST, JOIN_BAND_RESPONSE, CHANGE_SONG;
    }

    private  ENotifyType getTypeFromString(String type) {
        switch (type) {
            case "JOIN_BAND_REQUEST":
                return ENotifyType.JOIN_BAND_REQUEST;
            case "JOIN_BAND_RESPONSE":
               return ENotifyType.JOIN_BAND_RESPONSE;
            case "CHANGE_SONG":
                return ENotifyType.CHANGE_SONG;
        }
        return null;
    }



}
