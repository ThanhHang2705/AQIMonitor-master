package fimo.uet.fairapp.Service;

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

import java.util.ArrayList;

import fimo.uet.fairapp.Data.DataFromLocalHost;
import fimo.uet.fairapp.Data.GetDataFromAqicn;
import fimo.uet.fairapp.KQNode;
import fimo.uet.fairapp.R;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class MyService extends Service {
    MediaPlayer play;
    int dem =0;
    static SQLiteDatabase database;
    private final IBinder mBinder = new LocalBinder();
    private static boolean on_off_noti = false;
    String Address=null;
    int Conditional=-1;
    int PM25=0;
    String ID="";
    ArrayList<Data> ListNotificationOfNode ;
    ArrayList<KQNode>AllNodeList;
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
        AllNodeList = new ArrayList<>();
        database =this.openOrCreateDatabase("FeatureOfInterest.sqlite", getApplicationContext().MODE_PRIVATE,null);
//        getListNotification();
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
            this.Address = intent.getStringExtra("Address");
            this.Conditional = intent.getIntExtra("Conditional",this.Conditional);
            this.PM25 = intent.getIntExtra("PM",this.PM25);
            this.ID = intent.getStringExtra("ID");
            UpdateListNotification(this.ID, this.Conditional);
        }
//        UpdateDataFirst();
        UpdateData();
        showNotification();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
//        play.release();
        on_off_noti=false;
        Toast.makeText(getApplicationContext(),"Stop PM Service",Toast.LENGTH_LONG).show();
        super.onDestroy();

    }



    public void CreateNotification(){

        NotificationCompat.Builder mBuilder = null;
        NotificationManager mNotifyMgr;
        int mNotificationId = 1;
        mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        getListNotification();
        for (int i=0;i<ListNotificationOfNode.size();i++){
            double pm25 = ListNotificationOfNode.get(i).getPM25();
            int conditional = ListNotificationOfNode.get(i).getCondtional();
            if(ConditionalToNotification(conditional,pm25)){
                String name_node = ListNotificationOfNode.get(i).getAddress();
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                if(pm25>0) {
                    mBuilder =
                            (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                    .setSmallIcon(R.drawable.ic_app, 0)
                                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.icon_notify)).setVisibility(View.GONE)

                                    .setColor(Color.TRANSPARENT)

                                    .setContentTitle("PM: " + (int) pm25)
                                    .setSound(uri)
                                    .setContentText(name_node);

                }
                Notification notification = mBuilder.build();
                int smallIconId = getApplicationContext().getResources().getIdentifier("right_icon", "id", android.R.class.getPackage().getName());
                if (smallIconId != 0) {
                    notification.contentView.setViewVisibility(smallIconId, View.INVISIBLE);
                }
                // Builds the notification and issues it.
                mNotifyMgr.notify(mNotificationId, notification);
                mNotificationId++;
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
        }


    }

    public void showNotification(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try{
                        CreateNotification();
                        Thread.sleep(3600000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();



    }


    public boolean getOn_off_noti(){
        return on_off_noti;
    }

    public void getListNotification(){
        ListNotificationOfNode.clear();
        database =this.openOrCreateDatabase("FeatureOfInterest.sqlite", getApplicationContext().MODE_PRIVATE,null);
//        Cursor cursor = database.rawQuery("SELECT Data.ID, Data.NameNode, Data.PM, Notification.Conditional " +
//                "FROM Data INNER JOIN Notification ON Data.ID = Notification.ID",null);
        Cursor cursor = database.rawQuery("SELECT * FROM USER_DB", null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            for(int i=0;i<cursor.getCount();i++){
                String ID = cursor.getString(0);
                int conditional = cursor.getInt(1);
                Cursor cursor1 = database.rawQuery("SELECT Address FROM INFO_FAIR_BOX WHERE ID ='"+ID+"'",null);
                Cursor cursor2 = database.rawQuery("SELECT PM25 FROM INDEX_FAIR_BOX WHERE ID ='"+ID+"'", null);
                if (cursor1.getCount()>0&&cursor2.getCount()>0){
                    cursor1.moveToFirst();
                    String Address = cursor1.getString(0);
                    cursor2.moveToFirst();
                    double PM = cursor2.getDouble(0);
                    ListNotificationOfNode.add(new Data(ID,Address,PM,conditional));
                }
                cursor.moveToNext();
            }

        }
        cursor.close();
        database.close();
    }

    public void UpdateListNotification(String ID, int Conditional){
        String a = ID;
        if(ID !=null){
            if(ListNotificationOfNode.size()==0){

                database =this.openOrCreateDatabase("FeatureOfInterest.sqlite", getApplicationContext().MODE_PRIVATE,null);
                ContentValues contentValues = new ContentValues();
                contentValues.put("ID",ID);
                contentValues.put("Conditional",Conditional );
                contentValues.put("FAVOURITE", false);
                database.insert("USER_DB",null, contentValues);
                ID="";

            }else{
                for(int i=0;i<ListNotificationOfNode.size();i++){
                    String id = ListNotificationOfNode.get(i).getID();

                    if(ID.equals(id)){
                        database =this.openOrCreateDatabase("FeatureOfInterest.sqlite", getApplicationContext().MODE_PRIVATE,null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("Conditional",Conditional );
                        database.update("USER_DB", contentValues,"ID=?",new String[]{String.valueOf(ID)});
                        ID="";
                        break;
                    }else if(!ID.equals(id) && i==ListNotificationOfNode.size()-1){
                        database =this.openOrCreateDatabase("FeatureOfInterest.sqlite", getApplicationContext().MODE_PRIVATE,null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("ID",ID);
                        contentValues.put("Conditional",Conditional );
                        contentValues.put("FAVOURITE", false);
                        database.insert("USER_DB",null, contentValues);
                        ID="";
                        break;
                    }
                }
            }
            Address = null;
            database.close();
        }

    }

    public void setNotification(String Address, int Conditional, int PM){
        this.Address = Address;
        this.Conditional = Conditional;
        this.PM25 = PM;
//        UpdateListNotification();

    }

    public boolean ConditionalToNotification(int Conditional, double PM25){
        boolean state = false;
        if(Conditional == -1){

        }else if(Conditional == 0){
            state = true;
        }else if(Conditional == 1 && PM25>35){
            state = true;
        }else if(Conditional == 2 && PM25>75){
            state = true;
        }else if(Conditional == 3 && PM25>115){
            state = true;
        }else if(Conditional == 4 && PM25>150){
            state = true;
        }else if(Conditional == 5 && PM25>250){
            state = true;
        }else if(Conditional == 6 && PM25>350){
            state = true;
        }else if(Conditional==7){
            state = false;
        }
        return state;
    }

    public void UpdateDataFirst(){
//        getListNotification();
        for (int i=0;i<AllNodeList.size();i++){
            String id= AllNodeList.get(i).getID();
            if(id.equals("8767") || id.equals("8641")){
                GetDataFromAqicn getDataFromAqicn = new GetDataFromAqicn(MyService.this, id);
                getDataFromAqicn.execute();
                while(!getDataFromAqicn.isCancelled()){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    public void UpdateData(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true){
//                    getListNotification();
                    if(isConnected()){
//                        getAllNode();
                        for (int i=0;i<AllNodeList.size();i++){
                            String id= AllNodeList.get(i).getID();
                            if(id.equals("8767") || id.equals("8641")){
                                GetDataFromAqicn getDataFromAqicn = new GetDataFromAqicn(MyService.this, id);
                                getDataFromAqicn.execute();
                            }
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if(i==AllNodeList.size()-1){
                                i=0;
                            }
                        }
                    }

                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

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
    String ID;
    String Address;
    double PM25;
    int Condtional;
    public Data(String ID, String Address, double PM, int Conditional){
        this.ID = ID;
        this.Address = Address;
        this.PM25 = PM;
        this.Condtional=Conditional;
    }
    public String getID(){return ID;}
    public String getAddress(){return Address;}
    public double getPM25 (){return PM25;}
    public int getCondtional(){return Condtional;}
}