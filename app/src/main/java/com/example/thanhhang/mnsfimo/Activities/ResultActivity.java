package com.example.thanhhang.mnsfimo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.thanhhang.mnsfimo.Adapters.KQuaAdapter;
import com.example.thanhhang.mnsfimo.KQNode;
import com.example.thanhhang.mnsfimo.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView lv_KQ;
    ArrayList<KQNode> listKQ= new ArrayList<>();
    KQuaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_result);
        lv_KQ = (ListView) findViewById(R.id.lv_Kq);
        getListKQ(listKQ);
        adapter = new KQuaAdapter(listKQ,this);
        lv_KQ.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lv_KQ.setOnItemClickListener(this);
    }
    private void getListKQ(ArrayList<KQNode> a){
         a.add(new KQNode(1,"ĐHQGHN","Xuân Thủy","2.5",20,60,new LatLng(24,35)));
        a.add(new KQNode(1,"ĐHQGHN","Xuân Thủy","2.5",20,60,new LatLng(24,35)));
        a.add(new KQNode(1,"ĐHQGHN","Xuân Thủy","2.5",20,60,new LatLng(24,35)));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getApplicationContext(),Detail.class));
    }
}
