package com.zpjj.musicapp.musicianmanagementapp.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.zpjj.musicapp.musicianmanagementapp.R;
import com.zpjj.musicapp.musicianmanagementapp.models.Song;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by daniel on 28.01.17.
 */

public class NotifyService {
    private static final String TAG = "Notify Service";
    public String serverKey = null;

    public NotifyService(Context activity) {
        RxFirebaseDatabase.observeSingleValueEvent(FirebaseDatabase.getInstance().getReference("firebaseServer")).subscribe(
                dataSnapshot -> {
                    if (dataSnapshot.hasChild("key")) {
                        serverKey = dataSnapshot.child("key").getValue().toString();
                        SharedPreferences sharedPref = activity.getSharedPreferences(
                               activity.getString(R.string.key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(activity.getString(R.string.key), serverKey);
                        editor.commit();
                    }
                }
        );
    }

    public NotifyService(String serverKey) {
        this.serverKey = serverKey;
    }

    public void sendChangeSongNotification(String to, Song song) {
        Notification n = new Notification(song.getAuthor() + " - " + song.getTitle(), "Zmiana piosenki");
        try {
            sendNotification(n, to, FirebaseService.ENotifyType.CHANGE_SONG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAcceptJoinBandNotification(String to) {
        Notification n = new Notification("Zakceptowano prośbę dołączenia do zespołu", "Band App");
        try {
            sendNotification(n, to, FirebaseService.ENotifyType.JOIN_BAND_RESPONSE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRejectJoinBandNotification(String to) {
        Notification n = new Notification("Odrzucono prośbę dołączenia do zespołu", "Band App");
        try {
            sendNotification(n, to, FirebaseService.ENotifyType.JOIN_BAND_RESPONSE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendJoinRequestNotification(String to) {
        Notification n = new Notification("Nowa prośba o dołączenie do zespołu", "Band App");
        try {
            sendNotification(n, to, FirebaseService.ENotifyType.JOIN_BAND_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendTestNotification() {
        Notification n = new Notification("Title", "Body");
        try {
            sendNotification(n, "fvTOrBNf7q0:APA91bEsXPWXCygNVQYlZGQn2y-EVBxaVozNLy4HR-w15TzVGCmaBj7KLvcSKLPbQURR7KpRGfkPvkGiAq3_7NLNR0ldH3zneSFJBKWxx68Ek5ZQ24GJ84qshB2EX4GRTfvhKe89unZX", FirebaseService.ENotifyType.JOIN_BAND_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(Notification message, String to, FirebaseService.ENotifyType type) throws IOException {
        LoadingThread thread = new LoadingThread(message, to, serverKey, type);
        thread.start();
    }

    private class LoadingThread extends Thread {
        String serverKey;
        Object message;
        String to;
        String type;

        LoadingThread(Notification message, String to, String serverKey, FirebaseService.ENotifyType type) {
            this.message = message;
            this.to = to;
            this.serverKey = serverKey;
            this.type = type.toString();
        }

        @Override
        public void run() {
            super.run();

            HttpURLConnection con = null;
            try {
                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setConnectTimeout(999999999);
                con.setReadTimeout(999999999);
                con.setRequestProperty("Authorization", "key=" + this.serverKey);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");
                con.connect();
                Gson gson = new Gson();
                Map<String, Object> data = new HashMap<>();
                data.put("to", to);
                data.put("notification", message);
                HashMap<String, String> notifyData = new HashMap<>();
                notifyData.put("type", type);
                data.put("data", notifyData);
                Log.d(TAG, gson.toJson(data));
                OutputStream os = con.getOutputStream();
                os.write(gson.toJson(data).getBytes("UTF-8"));
                os.close();
                printConnection(con);
                InputStream is = con.getInputStream();
                String responseString = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }

        }

        private void printConnection(HttpURLConnection con) throws IOException {
            StringBuilder builder = new StringBuilder();
            builder.append(con.getResponseCode())
                    .append(" ")
                    .append(con.getResponseMessage())
                    .append("\n");

            Map<String, List<String>> map = con.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                if (entry.getKey() == null)
                    continue;
                builder.append(entry.getKey())
                        .append(": ");

                List<String> headerValues = entry.getValue();
                Iterator<String> it = headerValues.iterator();
                if (it.hasNext()) {
                    builder.append(it.next());

                    while (it.hasNext()) {
                        builder.append(", ")
                                .append(it.next());
                    }
                }

                builder.append("\n");
            }

            System.out.println(builder);
        }
    }

    private class Notification {
        private String body;
        private String title;

        public Notification(String body, String title) {
            this.body = body;
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
