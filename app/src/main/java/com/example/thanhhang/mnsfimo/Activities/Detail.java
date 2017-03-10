package com.example.thanhhang.mnsfimo.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.thanhhang.mnsfimo.Data.DataFromLocalHost;
import com.example.thanhhang.mnsfimo.R;
import com.example.thanhhang.mnsfimo.customdata.MyAxisValueFormatter;
import com.example.thanhhang.mnsfimo.customdata.TimeAxisValueFormatter;
import com.example.thanhhang.mnsfimo.customdata.XYMarkerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by HP on 1/15/2017.
 */

public class Detail extends AppCompatActivity implements OnChartValueSelectedListener {
    static int PM;
    String Address;
    private BarChart chart1, chart2, chart3;
    ArrayList<Long> Temperature, PM25, Humidity, AllData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DataFromLocalHost dataFromLocalHost = new DataFromLocalHost();
                dataFromLocalHost.execute();
                AllData = new ArrayList<>();
                try {
                    AllData = dataFromLocalHost.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
//                AllData = dataFromLocalHost.getAllData();
                while(AllData==null){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AllData = dataFromLocalHost.getAllData();
                }


                Temperature = new ArrayList<Long>();
                PM25 = new ArrayList<Long>();
                Humidity = new ArrayList<Long>();

                // Toàn bộ dữ liệu sau khi load về được để ở đây
                for (int i =0; i<AllData.size();i++){
                    if(i<24){
                        long a = AllData.get(i);
                        PM25.add(AllData.get(i));
                    }else if (i>=24 && i<49){
                        Temperature.add(AllData.get(i));
                    }else if(i>=48){
                        Humidity.add(AllData.get(i));
                    }
                }
//                Toast.makeText(Detail.this, s, Toast.LENGTH_LONG).show();
            }
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("TapTin");
        if (bundle != null) {
            PM = bundle.getInt("PM");
            Address = bundle.getString("Address");
        }

        chart1 = (BarChart) findViewById(R.id.bar_graph1);
        chart2 = (BarChart) findViewById(R.id.bar_graph2);
        chart3 = (BarChart) findViewById(R.id.bar_graph3);
        init(chart1, PM25,setColorForPM(PM25));
        ArrayList<Integer>color_temperature = new ArrayList<>();
        ArrayList<Integer>color_humidity = new ArrayList<>();
        color_temperature.add(Color.parseColor("#CDDC39"));
        init(chart2, Temperature,color_temperature);
        color_humidity.add(Color.parseColor("#0091EA"));
        init(chart3, Humidity, color_humidity);

    }

    private void init(BarChart chart, ArrayList<Long> Data, ArrayList<Integer>color) {
        chart.setOnChartValueSelectedListener(this);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setDrawValueAboveBar(true);
        chart.setDoubleTapToZoomEnabled(true);
        chart.setPinchZoom(true);
        chart.setMaxVisibleValueCount(60);
        IAxisValueFormatter xAxisFormatter = new TimeAxisValueFormatter();
           chart.setDescription(null);    // Hide the description
//        chart.getAxisLeft().setDrawLabels(false);
//        chart.getAxisRight().setDrawLabels(false);
//        chart.getXAxis().setDrawLabels(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(Color.BLACK);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);


        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

           Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        l.setEnabled(false);

        XYMarkerView mv = new XYMarkerView(getApplicationContext(), xAxisFormatter);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart

        setData(23, 1000,chart, Data, color);
    }

    private void setData(int count, float range, BarChart chart, ArrayList<Long> Data, ArrayList<Integer> Color) {

        float start = 1f;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < count ; i++) {
//            float mult = (range + 1);
            float val = (float) Data.get(i);
            yVals1.add(new BarEntry(i+1, val));
        }

        BarDataSet set1;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Time of a day");
            set1.setLabel("");
            set1.setColors(Color);
//            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
//            set1.setBarShadowColor(Color.BLACK);
//            set1.setHighlightEnabled(false);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);
            data.setDrawValues(false);
            chart.setData(data);
        }
    }

    public ArrayList<Integer> setColorForPM(ArrayList<Long>PM25){
        ArrayList<Integer>color = new ArrayList<>();
        for (int i =0;i<PM25.size();i++){
            if(PM25.get(i)<=50){
                color.add(Color.parseColor("#00C853"));
            }else if(PM25.get(i)>50 && PM25.get(i)<101){
                color.add(Color.parseColor("#FFEB3B"));
            }else if(PM25.get(i)>100 && PM25.get(i)<151){
                color.add(Color.parseColor("#FF6F00"));
            }else if(PM25.get(i)>150 && PM25.get(i)<201){
                color.add(Color.parseColor("#DD2C00"));
            }else if(PM25.get(i)>200){
                color.add(Color.parseColor("#C51162"));
            }
        }
        return color;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
    }

    @Override
    public void onNothingSelected() {
    }

}
