package com.zpjj.musicapp.musicianmanagementapp.services;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
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

public class NotifyService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";
    private String serverKey = null;
    private String senderId = null;

    public NotifyService() {
        RxFirebaseDatabase.observeSingleValueEvent(FirebaseDatabase.getInstance().getReference("firebaseServer")).subscribe(
                dataSnapshot -> {
                    if(dataSnapshot.hasChild("id")) {
                        senderId = dataSnapshot.child("id").getValue().toString();
                    }
                    if(dataSnapshot.hasChild("key")) {
                        serverKey = dataSnapshot.child("key").getValue().toString();
                    }
                }
        );
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Toast toast = Toast.makeText(getApplicationContext(), "Notification", Toast.LENGTH_LONG);
        toast.show();
    }



    public void notifyBandMasterAboutJoinRequest(Band selectedItem) {
//        UserService userService = new UserService();
//        userService.getUserInfo(selectedItem.getMasterUID()).subscribe(u -> {
//            try {
//                sendNotification(selectedItem, u.getFirebaseToken());
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        });
    }

    private void sendNotification(Object message, String to) throws IOException, JSONException {
        Handler h = new Handler();

        LoadingThread thread = new LoadingThread(message,to, serverKey);
        thread.start();

    }

    private class LoadingThread extends Thread {
        String serverKey;
        Object message;
        String to;

        LoadingThread(Object message, String to, String serverKey) {
            this.message = message;
            this.to = to;
            this.serverKey = serverKey;
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
                JSONObject data = new JSONObject();
                data.put("to", to);
                data.put("notification", gson.toJson(new Notification("Tytuł", "Body")));
                Log.d(TAG,data.toString());
                OutputStream os = con.getOutputStream();
                os.write(data.toString().getBytes("UTF-8"));
                os.close();


                printConnection(con);



                InputStream is = con.getInputStream();
                String responseString = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(con!= null) {
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
            for (Map.Entry<String, List<String>> entry : map.entrySet())
            {
                if (entry.getKey() == null)
                    continue;
                builder.append( entry.getKey())
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
