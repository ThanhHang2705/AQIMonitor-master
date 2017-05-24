package fimo.uet.fairapp.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

import fimo.uet.fairapp.Data.GetData24h;
import fimo.uet.fairapp.R;

/**
 * Created by HP on 5/8/2017.
 */

public class LoaderUI extends AppCompatActivity {
    String PM25, Temperature, Humidity;
    int PM;
    String ID, NameNode;
    int currentFragment = -1;
    Bundle bundle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PM25 = "";
        Temperature = "";
        Humidity = "";
        ID="";
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("TapTin");
        if (bundle != null) {
            PM = bundle.getInt("PM");
            ID = bundle.getString("ID");
            NameNode = bundle.getString("Address");
            currentFragment = bundle.getInt("CurrentFragment");
            new ProgressTask(LoaderUI.this, ID).execute();
        }else{
            finish();
        }


    }

    public class ProgressTask extends AsyncTask<String, String, Integer> {
        GetData24h getData24h;
        private ProgressDialog dialog;
        private Activity activity;
        String ID;

        public ProgressTask(Activity activity, String ID) {
            this.ID = ID;
            this.activity = activity;
            dialog = new ProgressDialog(activity);
            getData24h = new GetData24h(activity,ID);
        }


        protected void onPreExecute() {
            this.dialog.setMessage(activity.getString(R.string.data_processing));
            this.dialog.show();
        }

        @Override


        protected void onPostExecute(Integer integer) {

//            AllData=s;
//            if(AllData=="") {
//                Toast.makeText(activity, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
//            }else if(AllData.equals("Không có dữ liệu")){
//                Toast.makeText(activity, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
//            }
            if(integer>=200&&integer<300){
                ArrayList<Double> pm25 = new ArrayList<>();
                ArrayList<Double>temperature = new ArrayList<>();
                ArrayList<Double>humidity = new ArrayList<>();
                pm25 = getData24h.GetPM25();
                temperature = getData24h.GetTemperature();
                humidity = getData24h.GetHumidity();
                for(int i=0;i<pm25.size();i++){
                    if(i!=(pm25.size()-1)){
                        PM25+=pm25.get(i)+"\n";
                    }else {
                        PM25+=pm25.get(i);
                    }
                }
                for(int i=0;i<temperature.size();i++){
                    if(i!=(pm25.size()-1)){
                        Temperature+=temperature.get(i)+"\n";
                    }else {
                        Temperature+=temperature.get(i);
                    }
                }
                for(int i=0;i<humidity.size();i++){
                    if(i!=(humidity.size()-1)){
                        Humidity+=humidity.get(i)+"\n";
                    }else {
                        Humidity+=humidity.get(i);
                    }
                }

                dialog.dismiss();
                bundle.putString("PM25",PM25);
                bundle.putString("Temperature", Temperature);
                bundle.putString("Humidity", Humidity);
                Intent intent = new Intent(LoaderUI.this, Detail.class);
                intent.putExtra("TapTin", bundle);
                startActivity(intent);
                finish();
            }else{
                dialog.dismiss();
                Toast.makeText(LoaderUI.this,"Không lấy được dữ liệu",Toast.LENGTH_LONG).show();
            }


        }

        protected Integer doInBackground(final String... args) {
            return getData24h.getData();
        }
    }
}
