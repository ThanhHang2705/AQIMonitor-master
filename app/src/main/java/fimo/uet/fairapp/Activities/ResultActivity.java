package fimo.uet.fairapp.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import fimo.uet.fairapp.Adapters.KQuaAdapter;
import fimo.uet.fairapp.Data.GetData24h;
import fimo.uet.fairapp.KQNode;
import fimo.uet.fairapp.R;

public class ResultActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView lv_KQ;
    ArrayList<KQNode> listKQ= new ArrayList<>();
    ArrayList<KQNode> ListNode= new ArrayList<>();
    ArrayList<String> listResult = new ArrayList<>();
    KQuaAdapter adapter;
    String PM25, Temperature, Humidity;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        PM25 = "";
        Temperature = "";
        Humidity = "";
        setContentView(R.layout.activity_result);
        lv_KQ = (ListView) findViewById(R.id.lv_Kq);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("TapTin");
        if (bundle != null) {
//
            listResult = bundle.getStringArrayList("Result");
        }

        getListKQ(listKQ);
        adapter = new KQuaAdapter(listKQ,this);
        lv_KQ.setAdapter(adapter);
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) lv_KQ.getLayoutParams();

        lp.height = Resources.getSystem().getDisplayMetrics().heightPixels;
        lv_KQ.setLayoutParams(lp);
        adapter.notifyDataSetChanged();
        lv_KQ.setOnItemClickListener(this);
    }
    private void getListKQ(ArrayList<KQNode> a){
//         a.add(new KQNode(1,"ĐHQGHN","Xuân Thủy","2.5",20,60,new LatLng(24,35)));
        for (int i=0;i<listResult.size();i++){
            String[]s=listResult.get(i).split("\n");
            String ID = s[0];
            String Address = s[1];
            String NameNode = Address;
            double PM = Double.parseDouble(s[2]);
            double humidity = Double.parseDouble(s[3]);
            double temperature = Double.parseDouble(s[4]);
            LatLng latLng = new LatLng(Double.parseDouble(s[5]),Double.parseDouble(s[6]));
            a.add(new KQNode(ID,NameNode,Address,latLng,PM,humidity,temperature));
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        bundle = new Bundle();
        bundle.putString("ID", listKQ.get(position).getID());
        bundle.getString("Address", listKQ.get(position).getAddress());
        String ID = listKQ.get(position).getID();
        new ProgressTask(this,ID).execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
//                bundle = new Bundle();
//                bundle.putInt("CurrentFragment",2);
//                Intent intent = new Intent(ResultActivity.this,MainActivity.class);
//                intent.putExtra("TapTin",bundle);
//                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
            this.dialog.setMessage("Đang xử lý");
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
            dialog.dismiss();
            bundle.putInt("CurrentFragment", -1);
            bundle.putString("PM25",PM25);
            bundle.putString("Temperature", Temperature);
            bundle.putString("Humidity", Humidity);
            Intent intent = new Intent(activity,Detail.class);
            intent.putExtra("TapTin", bundle);
            startActivity(intent);
        }

        protected Integer doInBackground(final String... args) {
            getData24h.getData();
            ArrayList<Double>pm25 = new ArrayList<>();
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
            return 1;
        }
    }
}
