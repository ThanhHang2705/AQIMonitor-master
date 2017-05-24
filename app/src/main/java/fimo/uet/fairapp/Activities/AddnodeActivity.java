package fimo.uet.fairapp.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fimo.uet.fairapp.Adapters.AddNodeAdapter;
import fimo.uet.fairapp.MyLocation.LocationStatus;
import fimo.uet.fairapp.R;

public class AddnodeActivity extends AppCompatActivity {

    Button btn;
    TextView textView;
    WifiManager wifi;
    List<ScanResult> results;
    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    int size = 0;
    String ITEM_KEY = "key";
    private ListView lv_NewNode;
    private  ArrayList<String> listSSID = new ArrayList<>();
    private  ArrayList<Integer> listSignal = new ArrayList<>();
    private ArrayList<String> capability = new ArrayList<>();
    private AddNodeAdapter adapter;
    WifiManager.WifiLock wifiLock;
    LocationStatus locationStatus;
    @SuppressLint("WifiManagerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnode);
        locationStatus = new LocationStatus(this);
//        if(!locationStatus.isLocationServiceEnabled()){
//            locationStatus.EnableLocation();
//        }
        Intent intent = getIntent();
        final Bundle bundle =intent.getBundleExtra("LatLng");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lv_NewNode = (ListView) findViewById(R.id.lv_NewNode);


        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiLock = ((WifiManager) this
                .getSystemService(Context.WIFI_SERVICE)).createWifiLock(
                WifiManager.WIFI_MODE_SCAN_ONLY, "WlanSilencerScanLock");
        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }

        ScanWifi();
        adapter = new AddNodeAdapter(listSSID,listSignal,this);
        lv_NewNode.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv_NewNode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                TextView NameNode = (TextView) lv_NewNode.getChildAt(position).findViewById(R.id.txt_NewNode);
                String name_node = NameNode.getText().toString();

                final LoginWifi loginWifi = new LoginWifi(AddnodeActivity.this,name_node, bundle);


            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        final ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.update);
        imageView.setColorFilter(Color.WHITE);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.leftMargin= 200;

        imageView.setLayoutParams(layoutParams);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if(action==MotionEvent.ACTION_DOWN){
                    ScanWifi();
                    adapter.notifyDataSetChanged();
                }
                return true;
            }


        });
        actionBar.setCustomView(imageView);

    }
    public void getList(ArrayList<HashMap<String, String>> WifiList, ArrayList<String> Result){
        for (int i=0;i<WifiList.size();i++){

        }

    }

    public void ScanWifi(){
        results = new ArrayList<ScanResult>();

        results = wifi.getScanResults();

        size = results.size();
        String SSID;
        String SecurityMode;
        int signal;
        boolean lock;

        listSSID.clear();
        listSignal.clear();
        for (int i =0;i<results.size();i++){
            String s = wifiLock.toString();
            SSID = results.get(i).SSID.toString();
            signal = wifi.calculateSignalLevel(results.get(i).level,5);
            wifiLock.acquire();
            listSSID.add(SSID);
            listSignal.add(signal);
            SecurityMode = results.get(i).capabilities;
            capability.add(results.get(i).capabilities);

        }
    }

    public String CheckSecurityMode(String capability){
        String[]security = {"WEP","PSK","WPA","WPA2"};
        String SecurityMode=null;
        for(int i=0;i<security.length;i++){
            if(capability.contains(security[i])){
                SecurityMode = security[i];
            }
        }
        return SecurityMode;
    }
}

class DataWifi{
    String SSID;
    String SecurityMode;
    int signal;
    boolean lock;
    public DataWifi(String SSID, String SecurityMode,int signal, boolean lock){
        this.SSID = SSID;
        this.SecurityMode = SecurityMode;
        this.signal=signal;
        this.lock=lock;
    }

    public String getSSID(){
        return SSID;
    }

    public String getSecurityMode(){
        return SecurityMode;
    }
}