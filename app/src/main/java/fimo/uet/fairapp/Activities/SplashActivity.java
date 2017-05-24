package fimo.uet.fairapp.Activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import fimo.uet.fairapp.Data.Database;
import fimo.uet.fairapp.DatabaseManager.Index_Fair_Box_DB;
import fimo.uet.fairapp.DatabaseManager.Info_Fair_Box_DB;
import fimo.uet.fairapp.DatabaseManager.User_DB;
import fimo.uet.fairapp.KQNode;
import fimo.uet.fairapp.MainActivity;
import fimo.uet.fairapp.R;
import fimo.uet.fairapp.Service.MyService;

public class SplashActivity extends AppCompatActivity {
    ProgressBar progressBar;
    private int status = 0;
    private Handler mHandler = new Handler();
    ImageView imageView;

    ArrayList<KQNode> AllNodeList;

    /** Called when the activity is first created. */
    Thread splashTread;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        List< ActivityManager.RunningTaskInfo > runningTaskInfo = am.getRunningTasks(1);

////        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
//        if(componentInfo.getShortClassName().equals(".MainActivity")){
//            startActivity(new Intent(this, MainActivity.class));
//        }
        AllNodeList = new ArrayList<>();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        if(isConnected()){
            int dem = 0;
            new Progress(SplashActivity.this).execute();
//            while (dem <10){
//                try {
//                    Thread.sleep(1000);
//                    dem++;
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            User_DB user_db = new User_DB(this);
//            user_db.CreateTable();
//            Intent intent = new Intent(this, MyService.class);
//            startService(intent);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
//            finish();
//            Toast.makeText(SplashActivity.this,"Không tải được dữ liệu", Toast.LENGTH_LONG).show();

        }else {
            CreateAlertDialog(this);
//            finish();
        }

    }



    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public void getAllNode(){
        AllNodeList.clear();
        SQLiteDatabase database = this.openOrCreateDatabase("FeatureOfInterest.sqlite", getApplicationContext().MODE_PRIVATE,null);
        Cursor cursor = database.rawQuery("SELECT * FROM Data", null);
        int size = cursor.getCount();
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            String ID = cursor.getString(0);
            String NameNode = cursor.getString(1);
            String Address = cursor.getString(2);
            LatLng latLng = new LatLng(cursor.getDouble(3),cursor.getDouble(4));
            double PM = cursor.getInt(5);
            double Temperature = cursor.getInt(6);
            double Humidity = cursor.getInt(7);
            KQNode kqNode = new KQNode(ID,NameNode,Address,latLng,PM,Humidity,Temperature);
            AllNodeList.add(kqNode);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
    }


    class Progress extends AsyncTask<Integer,Integer,Integer>{
        Context context;
        private ProgressDialog dialog;
        public Progress(Context context){
            this.context = context;

        }


        @Override
        protected Integer doInBackground(Integer... params) {
            final int[] ResponseCode = {0};
            final Info_Fair_Box_DB info_fair_box_db = new Info_Fair_Box_DB(context);
            info_fair_box_db.CreateTable();
//            info_fair_box_db.RemoveAllData();
            final Index_Fair_Box_DB index_fair_box_db = new Index_Fair_Box_DB(context);
            index_fair_box_db.CreateTable();
            SQLiteDatabase db = Database.initDatabase(SplashActivity.this, "FeatureOfInterest.sqlite");
            Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

            if (c.moveToFirst()) {
                while ( !c.isAfterLast() ) {
                    String table = c.getString(0);
                    c.moveToNext();
                }
            }
            User_DB user_db = new User_DB(context);
            user_db.CreateTable();

//                    ResponseCode[0] = info_fair_box_db.UpdateDatabase();
//                    if(ResponseCode[0] >=200 && ResponseCode[0] <300){
////                        ResponseCode[0] = index_fair_box_db.UpdateDatabase();
//                    }


//            int interval = 0;
//            Calendar calendar = Calendar.getInstance();
//            long now = calendar.getTimeInMillis();
//            long timestamp = now / 1000;
//            while (interval<10){
//                calendar = Calendar.getInstance();
//                now = calendar.getTimeInMillis();
//                long current_timestamp = now / 1000;
//                interval = (int) (current_timestamp-timestamp);
//            }
            Intent intent = new Intent(context, MyService.class);
            startService(intent);
            intent = new Intent(context, MainActivity.class);
            startActivity(intent);

            return ResponseCode[0];
        }

        @Override
        protected void onPreExecute() {


            new Thread(new Runnable() {
                @Override
                public void run() {
                    StyleAnimation2
                            (context);
                }
            }).start();



        }



        @Override
        protected void onPostExecute(Integer integer) {
//            dialog.dismiss();
            if(integer>=200 && integer<300){
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(SplashActivity.this, "Không lấy được dữ liệu",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            }
            /*Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(context,"Không tải được dữ liệu", Toast.LENGTH_LONG).show();*/
        }
    }



    public void StyleAnimation1(Context context){
        Animation anim1 = AnimationUtils.loadAnimation(context, R.anim.alpha);
        anim1.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim1);

        anim1 = AnimationUtils.loadAnimation(context, R.anim.translate);
        anim1.setDuration(2000);
        anim1.reset();

        Animation anim2 = AnimationUtils.loadAnimation(context,R.anim.rotate);
        anim2.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setFillAfter(false);
        animationSet.setFillBefore(true);
        animationSet.setFillEnabled(true);
        animationSet.addAnimation(anim1);
        animationSet.addAnimation(anim2);
        animationSet.start();
//        iv.clearAnimation();
//        iv.startAnimation(animationSet);

    }

    public void StyleAnimation2(Context context){
        Animation anim1 = AnimationUtils.loadAnimation(context, R.anim.alpha);
        anim1.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim1);
        anim1 = AnimationUtils.loadAnimation(context,R.anim.rotate);
        anim1.setDuration(3000);
        anim1.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim1);
    }


    public void CreateAlertDialog(Context context){
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setMessage(R.string.enable_internet);
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent myIntent = new Intent( Settings.ACTION_SETTINGS);
//                startActivity(myIntent);
                finish();
            }
        });
        adb.show();
    }
}