package com.example.thanhhang.mnsfimo.Service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.thanhhang.mnsfimo.Data.DataFromLocalHost;
import com.example.thanhhang.mnsfimo.KQNode;
import com.example.thanhhang.mnsfimo.R;

import java.util.ArrayList;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class MyService extends Service {
    MediaPlayer play;
    int dem =0;
    static SQLiteDatabase database;
    private final IBinder mBinder = new LocalBinder();
    private static boolean on_off_noti = false;
    String NameNode=null;
    int Conditional=-1;
    int PM25=0;
    ArrayList<Data> ListNotificationOfNode ;
    ArrayList<KQNode>FavouriteList;
    DataFromLocalHost dataFromLocalHost;
    ArrayList<Long>PM_25,Temperature,Humidity;
    public MyService() {
    }


    public class LocalBinder extends Binder {

        public MyService getService()  {
            return MyService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.i(LOG_TAG,"onBind");
        // TODO: Return the communication channel to the service.
        return mBinder;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(LOG_TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(LOG_TAG, "onRebind");
        super.onRebind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ListNotificationOfNode = new ArrayList<>();
        database =this.openOrCreateDatabase("FeatureOfInterest.sqlite", getApplicationContext().MODE_PRIVATE,null);
        getListNotification();
        Temperature = new ArrayList<>();
        PM_25 = new ArrayList<>();
        Humidity = new ArrayList<>();


//        play = MediaPlayer.create(getApplicationContext(), R.raw.badthings);
        Toast.makeText(getApplicationContext(), "PM Service running",Toast.LENGTH_LONG).show();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        play.start();
        if(intent!=null){
            this.NameNode = intent.getStringExtra("NameNode");
            this.Conditional = intent.getIntExtra("Conditional",this.Conditional);
            this.PM25 = intent.getIntExtra("PM",this.PM25);
            UpdateListNotification(NameNode,PM25,Conditional);
        }
        SendNotification();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
//        play.release();
        on_off_noti=false;
        Toast.makeText(getApplicationContext(),"Stop PM Service",Toast.LENGTH_LONG).show();
        super.onDestroy();

    }



    public void CreateNotification(double pm25, String name_node){

        NotificationCompat.Builder mBuilder;
        NotificationManager mNotifyMgr;
        int mNotificationId = 001;
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_app,0)
                        .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.icon_notify)).setVisibility(View.GONE)

                        .setColor(Color.TRANSPARENT)

                        .setContentTitle("PM: "+pm25)
                        .setSound(uri)
                        .setContentText(name_node);


        Notification notification = mBuilder.build();
        int smallIconId = getApplicationContext().getResources().getIdentifier("right_icon", "id", android.R.class.getPackage().getName());
        if (smallIconId != 0) {
            notification.contentView.setViewVisibility(smallIconId, View.INVISIBLE);

        }
        mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, notification);
//        int smallIconId = getBaseContext().getResources().getIdentifier("right_icon", "id", android.R.class.getPackage().getName());
//        if (smallIconId != 0) {
//            mBuilder.bigContentView.setViewVisibility(smallIconId, View.INVISIBLE);
//        }


        Intent resultIntent = new Intent(getApplicationContext(), MyService.class);
        resultIntent.putExtra("content", "PM: null\n");

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        // Set content intent;
        mBuilder.setContentIntent(resultPendingIntent);
    }

    public void SendNotification(){

        new Thread(new Runnable(){
            public void run() {
                // TODO Auto-generated method stub

                while(true)
                {
                    if(isConnected()){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
//                                UpdateData();

                            }
                        }).start();
                    }
                    getListNotification();
                    for (int i=0;i<ListNotificationOfNode.size();i++){

                        if(PM_25.size()>0){
                            PM25=PM_25.get(0).intValue();
                        }else{
                            double pm25=ListNotificationOfNode.get(i).getPM25();
                            String name_node = ListNotificationOfNode.get(i).getNameNode();
                            int conditional = ListNotificationOfNode.get(i).getCondtional();
                            ConditionalToNotification(conditional, name_node, pm25);
                        }
                        try {
                            Thread.sleep(15000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        Thread.sleep(15000);


//                        Toast.makeText(getApplicationContext(),String.valueOf(on_off_noti),Toast.LENGTH_LONG).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //REST OF CODE HERE//
                }

            }
        }).start();

    }

//    public void setOn_off_noti(boolean state){
//
//        this.on_off_noti = new SettingActivity().getCheckNotiy();
//    }

    public boolean getOn_off_noti(){
        return on_off_noti;
    }

    public void getListNotification(){
        ListNotificationOfNode.clear();
        database =this.openOrCreateDatabase("FeatureOfInterest.sqlite", getApplicationContext().MODE_PRIVATE,null);
        Cursor cursor = database.rawQuery("SELECT * FROM Notification",null);
        cursor.moveToFirst();
        for(int i=0; i<cursor.getCount();i++){
            String NameNode = cursor.getString(1);
            double PM = cursor.getDouble(2);
            int conditional = cursor.getInt(3);
            ListNotificationOfNode.add(new Data(NameNode,PM,conditional));
            cursor.moveToNext();
        }
    }
    public void UpdateListNotification(String NameNode, double PM25, int Conditional){
        if(NameNode!=null){
            if(ListNotificationOfNode.size()==0){

                database =this.openOrCreateDatabase("FeatureOfInterest.sqlite", getApplicationContext().MODE_PRIVATE,null);
                Cursor cursor = database.rawQuery("SELECT * FROM Notification",null);
                ContentValues contentValues = new ContentValues();
                contentValues.put("ID",ListNotificationOfNode.size()+1);
                contentValues.put("NAMENODE",NameNode);
                contentValues.put("PM25",PM25);
                contentValues.put("Conditional",Conditional );
                database.insert("Notification",null, contentValues);

            }else{
                for(int i=0;i<ListNotificationOfNode.size();i++){
                    String name_node = ListNotificationOfNode.get(i).getNameNode();

                    if(NameNode.equals(name_node)){
                        database =this.openOrCreateDatabase("FeatureOfInterest.sqlite", getApplicationContext().MODE_PRIVATE,null);
                        Cursor cursor = database.rawQuery("SELECT * FROM Notification",null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("PM25",PM25);
                        contentValues.put("Conditional",Conditional );
                        database.update("Notification", contentValues,"NAMENODE=?",new String[]{NameNode});
                        break;
                    }else if(!NameNode.equals(name_node) && i==ListNotificationOfNode.size()-1){
                        database =this.openOrCreateDatabase("FeatureOfInterest.sqlite", getApplicationContext().MODE_PRIVATE,null);
                        Cursor cursor = database.rawQuery("SELECT * FROM Notification",null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("ID",ListNotificationOfNode.size()+1);
                        contentValues.put("NAMENODE",NameNode);
                        contentValues.put("PM25",PM25);
                        contentValues.put("Conditional",Conditional );
                        database.insert("Notification",null, contentValues);
                        break;
                    }
                }
            }
            NameNode = null;

        }

    }

    public void setNotification(String NameNode, int Conditional, int PM){
        this.NameNode = NameNode;
        this.Conditional = Conditional;
        this.PM25 = PM;
//        UpdateListNotification();

    }

    public void ConditionalToNotification(int Conditional, String NameNode, double PM25){

        if(Conditional == -1){

        }else if(Conditional == 0){
            CreateNotification(PM25,NameNode);
        }else if(Conditional == 1){
            if(PM25>35){
                CreateNotification(PM25,NameNode);
            }
        }else if(Conditional == 2){
            if(PM25>75){
                CreateNotification(PM25,NameNode);
            }
        }else if(Conditional == 3){
            if(PM25>115){
                CreateNotification(PM25,NameNode);
            }
        }else if(Conditional == 4){
            if(PM25>150){
                CreateNotification(PM25,NameNode);
            }
        }else if(Conditional == 5){
            if(PM25>250){
                CreateNotification(PM25,NameNode);
            }
        }else if(Conditional == 6){
            if(PM25>350){
                CreateNotification(PM25,NameNode);
            }
        }else if(Conditional==7){

        }

    }

    public void UpdateData(){
//        dataFromLocalHost = new DataFromLocalHost();
//        dataFromLocalHost.execute();
//        String AllData = null;
//        try {
//            AllData = dataFromLocalHost.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        int size ;
//
//        dataFromLocalHost.ParseJsonData(AllData,PM_25,Temperature,Humidity);
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
}

class Data{
    String NameNode;
    double PM25;
    int Condtional;
    public Data(String NameNode, double PM, int Conditional){
        this.NameNode = NameNode;
        this.PM25 = PM;
        this.Condtional=Conditional;
    }

    public String getNameNode(){return NameNode;}
    public double getPM25 (){return PM25;}
    public int getCondtional(){return Condtional;}
}