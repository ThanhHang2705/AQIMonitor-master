package com.example.thanhhang.mnsfimo.Activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.ImageView;

import com.example.thanhhang.mnsfimo.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgNotify;
    private ImageView imgWitget;
    private ImageView imgAutoLoad;
    private Boolean checkNotify= false, checkWitget= false , checkAutoLoad = false;

    NotificationCompat.Builder mBuilder;
    NotificationManager mNotifyMgr;
    int mNotificationId = 001;
    String strContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        imgNotify = (ImageView) findViewById(R.id.imgNotify);
        imgWitget = (ImageView) findViewById(R.id.imgWitget);
        imgAutoLoad = (ImageView) findViewById(R.id.imgAutoLoad);

        imgNotify.setOnClickListener(this);
        imgWitget.setOnClickListener(this);
        imgAutoLoad.setOnClickListener(this);

        strContent = "Welcome to PM2.5";

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgNotify:
                checkNotify= !checkNotify;
                if(checkNotify){
                    imgNotify.setImageResource(R.drawable.turn_on);
                    mBuilder =
                            (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                    .setSmallIcon(R.drawable.ic_app)
                                    .setContentTitle("My notification")
                                    .setContentText(strContent);

                    mNotifyMgr =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    // Builds the notification and issues it.
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());


                    Intent resultIntent = new Intent(getApplicationContext(), ResultActivity.class);
                    resultIntent.putExtra("content", strContent);

                    PendingIntent resultPendingIntent =
                            PendingIntent.getActivity(
                                    getApplicationContext(),
                                    0,
                                    resultIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    // Set content intent;
                    mBuilder.setContentIntent(resultPendingIntent);
                }else{
                    imgNotify.setImageResource(R.drawable.turn_off);
                    mNotifyMgr.cancel(mNotificationId);

                }



                break;
            case R.id.imgWitget:
                checkWitget= !checkWitget;
                if(checkWitget){
                    imgWitget.setImageResource(R.drawable.turn_on);
                }else{
                    imgWitget.setImageResource(R.drawable.turn_off);
                }
                break;
            case  R.id.imgAutoLoad:
                checkAutoLoad= !checkAutoLoad;
                if(checkAutoLoad){
                    imgAutoLoad.setImageResource(R.drawable.turn_on);
                }else{
                    imgAutoLoad.setImageResource(R.drawable.turn_off);
                }
                break;
        }

    }
}
