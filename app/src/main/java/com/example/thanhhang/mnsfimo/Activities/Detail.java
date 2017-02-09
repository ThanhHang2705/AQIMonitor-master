package com.example.thanhhang.mnsfimo.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.thanhhang.mnsfimo.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by HP on 1/15/2017.
 */

public class Detail extends AppCompatActivity {

    BarChart chart ;
    ArrayList<BarEntry> BARENTRY ;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset ;
    BarData BARDATA;
    static int PM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("TapTin");
        if(bundle != null){
            PM = bundle.getInt("PM");
        }

        /*Toast.makeText(this,PM,Toast.LENGTH_LONG).show();*/
        BarChart chart = (BarChart) findViewById(R.id.bar_graph);
        BarChart chart2 = (BarChart) findViewById(R.id.bar_graph2);
        BarChart chart3 = (BarChart) findViewById(R.id.bar_graph3);

        chart.getXAxis().setEnabled(false);
       /* chart.setDrawBarShadow(false);*/
        chart.getAxisRight().setEnabled(false);
        chart.setDrawBarShadow(false);

        chart2.getXAxis().setEnabled(false);
        chart2.getAxisRight().setEnabled(false);
        chart2.setDrawBarShadow(false);

        chart3.getXAxis().setEnabled(false);
        chart3.getAxisRight().setEnabled(false);
        chart3.setDrawBarShadow(false);
        BARENTRY = new ArrayList<>();

        BarEntryLabels = new ArrayList<String>();

        AddValuesToBARENTRY();

        AddValuesToBarEntryLabels();

        Bardataset = new BarDataSet(BARENTRY, "Projects");

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        /*dataSets.add(BarEntryLabels);*/
        BARDATA = new BarData(Bardataset);
        BARDATA.setBarWidth(0.9f);

        ArrayList<Integer> colors = new ArrayList<>();
        for(int i=0;i<BARENTRY.size();i++){

            BarEntry barEntry = BARENTRY.get(i);
            if(barEntry.getY()<=50){
                colors.add(Color.rgb(9, 242, 71));

            }else if(barEntry.getY()<=100 && barEntry.getY()>50){
                colors.add(Color.rgb(236, 252, 10));

            }
        }

        Bardataset.setColors(colors);

        chart.setData(BARDATA);
        chart2.setData(BARDATA);
        chart3.setData(BARDATA);


    }

    public void AddValuesToBARENTRY(){
        ArrayList<Integer> PMToInsert = new ArrayList<>();
        /*PMToInsert.add(PM);*/
        if(PMToInsert.size()>24){
            PMToInsert.remove(0);
            for(int i=0;i<24;i++){
                PMToInsert.add(PMToInsert.get(i+1));
                PMToInsert.remove(24);
            }
        }
        if (PMToInsert.size()<=24){
            for(int i=0;i<24;i++){
                Random random = new Random();
                int PM_Temp = random.nextInt(91)+1;
                PMToInsert.add(PM_Temp);
                if(i==23){
                    PMToInsert.add(PM);
                }
            }
        }
        for(int i = 24;i>0;i--){
            BARENTRY.add(new BarEntry(i, PMToInsert.get(i)));
        }
        /*BARENTRY.add(new BarEntry(0,PMToInsert.get(24)));*/
    }

    public void AddValuesToBarEntryLabels(){

        BarEntryLabels.add("January");
        BarEntryLabels.add("February");
        BarEntryLabels.add("March");
        BarEntryLabels.add("April");
        BarEntryLabels.add("May");
        BarEntryLabels.add("June");

    }

}
