package com.example.thanhhang.mnsfimo.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.thanhhang.mnsfimo.MainActivity;
import com.example.thanhhang.mnsfimo.R;
import com.example.thanhhang.mnsfimo.Service.MyService;

/**
 * Created by HP on 3/15/2017.
 */

public class Notification extends AppCompatActivity{
    RadioGroup PM_Notification;
    RadioButton RB;
    Button OK, Cancel;
    String NameNode;
    int Conditional=-1;
    double PM25;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_api_alert);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("TapTin");
        String pm25 = null;
        if (bundle != null) {
            pm25= bundle.getString("PM25");
            NameNode= bundle.getString("NameNode");
        }
        OK = (Button)findViewById(R.id.notification_ok);
        Cancel = (Button)findViewById(R.id.notification_cancel);
        PM_Notification = (RadioGroup)findViewById(R.id.pm_notification);
        PM_Notification.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int ID = checkedId;
                int SelectedID = PM_Notification.getCheckedRadioButtonId();
                RB = (RadioButton)findViewById(SelectedID);
                Conditional = PM_Notification.indexOfChild(RB);
            }
        });
        PM25 = Double.valueOf(pm25);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("CurrentFragment",1);
                Intent intent = new Intent(Notification.this,MainActivity.class);
                intent.putExtra("TapTin",bundle);
                startActivity(intent);
                new MyService().setNotification(NameNode,Conditional,(int)PM25);

            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("CurrentFragment",1);
                Intent intent = new Intent(Notification.this,MainActivity.class);
                intent.putExtra("TapTin",bundle);
                startActivity(intent);
            }
        });



    }
}
