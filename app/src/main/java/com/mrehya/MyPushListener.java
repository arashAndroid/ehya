package com.mrehya;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import co.ronash.pushe.PusheListenerService;

/**
 * Created by ashke98 on 6/6/2018.
 */

public class MyPushListener extends PusheListenerService {

    @Override
    public void onMessageReceived(JSONObject message, JSONObject content){
        android.util.Log.i("Pushe","Custom json Message: "+ message.toString());
        // Your Code

        //your code
        try{
            Intent i =  new Intent(this, Language.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            String s1 = message.getString("title");
            String s2 = message.getString("content");
            android.util.Log.i("Pushe","Json Message\n Titr: " + s1 + "\n Matn: " + s2);

            PendingIntent pendingintent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle(s1)
                    .setContentText(s2)
                    .setSmallIcon(R.drawable.ic_notif)
                    .setContentIntent(pendingintent);

            NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());

        } catch (JSONException e) {
            android.util.Log.e("","Exception in parsing json" ,e);
        }

    }

}
