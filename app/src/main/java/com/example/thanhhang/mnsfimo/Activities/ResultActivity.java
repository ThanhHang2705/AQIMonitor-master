package com.example.thanhhang.mnsfimo.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.thanhhang.mnsfimo.Adapters.KQuaAdapter;
import com.example.thanhhang.mnsfimo.KQNode;
import com.example.thanhhang.mnsfimo.MainActivity;
import com.example.thanhhang.mnsfimo.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView lv_KQ;
    ArrayList<KQNode> listKQ= new ArrayList<>();
    ArrayList<KQNode> ListNode= new ArrayList<>();
    ArrayList<String> listResult = new ArrayList<>();
    KQuaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            int ID = Integer.parseInt(s[0]);
            String NameNode = s[1];
            String Address = s[2];
            String PM = s[3];
            int humidity = Integer.parseInt(s[4]);
            int temperature = Integer.parseInt(s[5]);
            LatLng latLng = new LatLng(Double.parseDouble(s[6]),Double.parseDouble(s[7]));
            a.add(new KQNode(ID,NameNode,Address,PM,humidity,temperature,latLng));
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getApplicationContext(),Detail.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Bundle bundle = new Bundle();
                bundle.putInt("CurrentFragment",2);
                Intent intent = new Intent(ResultActivity.this,MainActivity.class);
                intent.putExtra("TapTin",bundle);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
