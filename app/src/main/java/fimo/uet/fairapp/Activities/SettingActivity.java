package fimo.uet.fairapp.Activities;

import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fimo.uet.fairapp.R;

import fimo.uet.fairapp.Progress_Dialog.UpdateAllDB_Progress;

public class SettingActivity extends AppCompatActivity {

    private ImageView imgNotify;
    private ImageView imgWitget;
    private ImageView imgAutoLoad;
    private Boolean checkNotify= false, checkWitget= false , checkAutoLoad = false;

    NotificationCompat.Builder mBuilder;
    NotificationManager mNotifyMgr;
    int mNotificationId = 001;
    String strContent;
    TextView UpdateDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        imgNotify = (ImageView) findViewById(R.id.imgNotify);
//        imgWitget = (ImageView) findViewById(R.id.imgWitget);
//        imgAutoLoad = (ImageView) findViewById(R.id.imgAutoLoad);
        UpdateDB = (TextView)findViewById(R.id.update_db);
        strContent = "Welcome to PM2.5";

        UpdateDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAllDB_Progress updateAllDB_progress = new UpdateAllDB_Progress(SettingActivity.this);
                updateAllDB_progress.execute();
            }
        });

    }



}
