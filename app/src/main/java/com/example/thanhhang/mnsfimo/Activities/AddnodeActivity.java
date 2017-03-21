package com.example.thanhhang.mnsfimo.Activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanhhang.mnsfimo.Adapters.AddNodeAdapter;
import com.example.thanhhang.mnsfimo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddnodeActivity extends AppCompatActivity {

    Button btn;
    TextView textView;
    WifiManager wifi;
    List<ScanResult> results;
    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    int size = 0;
    String ITEM_KEY = "key";
    private ListView lv_NewNode;
    private  ArrayList<String> listNewNode = new ArrayList<>();
    private ArrayList<String> capability = new ArrayList<>();
    private AddNodeAdapter adapter;
    @SuppressLint("WifiManagerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lv_NewNode = (ListView) findViewById(R.id.lv_NewNode);
        btn = (Button) findViewById(R.id.buttonScan);

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }
        registerReceiver(new BroadcastReceiver()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceive(Context c, Intent intent)
            {
                    results = wifi.getScanResults();
                size = results.size();
                String SSID;
                String SecurityMode;

                listNewNode.clear();
                for (int i =0;i<results.size();i++){
                    SSID = results.get(i).SSID.toString();
                    listNewNode.add(SSID);
                    SecurityMode = results.get(i).capabilities;
                    capability.add(results.get(i).capabilities);

                }
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arraylist.clear();
                wifi.startScan();

                Toast.makeText(AddnodeActivity.this, "Scanning...." + size, Toast.LENGTH_LONG).show();
                try
                {
                    size = size - 1;
                    while (size >= 0)
                    {
                        HashMap<String, String> item = new HashMap<String, String>();
                        item.put(ITEM_KEY, results.get(size).SSID + "  " + results.get(size).capabilities);

                        arraylist.add(item);
                        size--;
                        adapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                { }
            }
        });
        adapter = new AddNodeAdapter(listNewNode,this);
        lv_NewNode.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv_NewNode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView NameNode = (TextView) lv_NewNode.getChildAt(position).findViewById(R.id.txt_NewNode);
                String name_node = NameNode.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("capability",capability.get(position));
                bundle.putString("NameNode",name_node);
                Intent intent = new Intent(AddnodeActivity.this, ConnectNode.class);
                intent.putExtra("TapTin", bundle);
                startActivity(intent);
            }
        });
//        lv_NewNode = (ListView) findViewById(R.id.lv_NewNode);
//        getList(listNewNode);
//        adapter = new AddNodeAdapter(listNewNode,this);
//        lv_NewNode.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

    }
    public void getList(ArrayList<HashMap<String, String>> WifiList, ArrayList<String> Result){
        for (int i=0;i<WifiList.size();i++){

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
    public DataWifi(String SSID, String SecurityMode){
        this.SSID = SSID;
        this.SecurityMode = SecurityMode;
    }

    public String getSSID(){
        return SSID;
    }

    public String getSecurityMode(){
        return SecurityMode;
    }
}