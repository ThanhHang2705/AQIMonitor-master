package fimo.uet.fairapp.Activities;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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

import fimo.uet.fairapp.Data.Database;
import fimo.uet.fairapp.Data.GetData24h;
import fimo.uet.fairapp.DatabaseManager.Index_Fair_Box_DB;
import fimo.uet.fairapp.KQNode;
import fimo.uet.fairapp.R;
import fimo.uet.fairapp.Service.MyService;
import fimo.uet.fairapp.customdata.MyAxisValueFormatter;
import fimo.uet.fairapp.customdata.TimeAxisValueFormatter;
import fimo.uet.fairapp.customdata.XYMarkerView;

/**
 * Created by HP on 1/15/2017.
 */

public class Detail extends AppCompatActivity implements OnChartValueSelectedListener {
    String Address;
    String ID=null;
    int PM,Temp,Humd;
    int pm_color;
    int currentFragment = -1;
    private BarChart chart1, chart2, chart3;
    ArrayList<Double> Temperature, PM25, Humidity;
    String AllData="";
    TextView textView,pm,temperature, humidity;
    SQLiteDatabase database;
    int status_favourite= R.drawable.not_favourite;
    private static ArrayList<KQNode> FavouriteList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("TapTin");
        if(bundle!=null){
            Address = bundle.getString("Address");
            ID = bundle.getString("ID");
            currentFragment = bundle.getInt("CurrentFragment");
            String pm25 = bundle.getString("PM25");
            String temperature = bundle.getString("Temperature");
            String humidity = bundle.getString("Humidity");
            PM25 = new ArrayList<>();
            Temperature = new ArrayList<>();
            Humidity = new ArrayList<>();
            GetData24h getData24h = new GetData24h(this, null);
            PM25 = ParseData(pm25);
            Temperature = ParseData(temperature);
            Humidity = ParseData(humidity);
            SetUI();
        }else{

        }

//        getDataFromID();

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void SetUI(){
        setPMColor();
        textView = (TextView)findViewById(R.id.txt_nameNode_detail);
        pm = (TextView)findViewById(R.id.txt_pm_detail) ;
        temperature = (TextView)findViewById(R.id.txt_temp_detail) ;
        humidity = (TextView)findViewById(R.id.txt_hum_detail) ;
        textView.setText(Address);
        pm.setText(String.valueOf(PM25.get(PM25.size()-1)));
        pm.setTextColor(pm_color);
        temperature.setText(String.valueOf(Temperature.get(Temperature.size()-1)));
        humidity.setText(String.valueOf(Humidity.get(Humidity.size()-1)));
        Index_Fair_Box_DB index_fair_box_db = new Index_Fair_Box_DB(this);
        index_fair_box_db.UpdateIndexFromID(ID,PM25.get(PM25.size()-1),Temperature.get(Temperature.size()-1),
                Humidity.get(Humidity.size()-1));

        chart1 = (BarChart) findViewById(R.id.bar_graph1);
        chart2 = (BarChart) findViewById(R.id.bar_graph2);
        chart3 = (BarChart) findViewById(R.id.bar_graph3);
        init(chart1, PM25,setColorForPM(PM25),"PM 2.5");
        ArrayList<Integer>color_temperature = new ArrayList<>();
        ArrayList<Integer>color_humidity = new ArrayList<>();
        color_temperature.add(Color.parseColor("#CDDC39"));
        init(chart2, Temperature,color_temperature, "Temperature");
        color_humidity.add(Color.parseColor("#0091EA"));
        init(chart3, Humidity, color_humidity,"Humidity");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        final ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        checkNodeInFavourite();
        imageView.setImageResource(status_favourite);
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
                if(action== MotionEvent.ACTION_DOWN){

                    if(status_favourite==R.drawable.not_favourite){
                        status_favourite=R.drawable.favourite;
                        AddNodeToFavourite();
                    }else if(status_favourite==R.drawable.favourite){
                        status_favourite=R.drawable.not_favourite;
                        RemoveNodeFromFavourite();
                    }
                }
                imageView.setImageResource(status_favourite);
                return true;
            }


        });
        actionBar.setCustomView(imageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
//                int i = Intent.FLAG_ACTIVITY_CLEAR_TOP;
//                startActivityForResult(new Intent(getApplicationContext(),ResultActivity.class),Intent.FLAG_ACTIVITY_CLEAR_TOP);



                return true;
            case R.id.notificatioin_setting:
                final AlertDialog.Builder alertadd = new AlertDialog.Builder(Detail.this);
                LayoutInflater factory = LayoutInflater.from(Detail.this);
                final View view = factory.inflate(R.layout.level_api_alert, null);
                final AlertDialog alertDialog = alertadd.create();
                Button OK = (Button)view.findViewById(R.id.notification_ok);
                Button Cancel = (Button)view.findViewById(R.id.notification_cancel);
                final RadioGroup PM_Notification = (RadioGroup)view.findViewById(R.id.pm_notification);
                final int[] Conditional = new int[1];
                Conditional[0] = getConditional();
                PM_Notification.check(PM_Notification.getChildAt(Conditional[0]).getId());
                PM_Notification.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        int ID = checkedId;
                        int SelectedID = PM_Notification.getCheckedRadioButtonId();
                        RadioButton RB = (RadioButton)view.findViewById(SelectedID);
                        Conditional[0] = PM_Notification.indexOfChild(RB);
                    }
                });


                OK.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        Intent mServiceIntent = new Intent(getBaseContext(), MyService.class);
                        mServiceIntent.putExtra("Address", Address);
                        mServiceIntent.putExtra("Conditional",Conditional[0]);
                        mServiceIntent.putExtra("PM",PM);
                        mServiceIntent.putExtra("ID",ID);
                        startService(mServiceIntent);
//                        getSystemService(MyService.class).setNotification(NameNode,Conditional[0],PM);


                    }
                });

                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(view);
                alertDialog.show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);



        return super.onCreateOptionsMenu(menu);
    }

    private void init(BarChart chart, ArrayList<Double> Data, ArrayList<Integer>color, String type) {
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
        xAxis.setValueFormatter(xAxisFormatter);
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

        XYMarkerView mv = new XYMarkerView(getApplicationContext(), xAxisFormatter, type);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart

        setData(23, 1000,chart, Data, color);
    }

    private void setData(int count, float range, BarChart chart, ArrayList<Double> Data, ArrayList<Integer> Color) {

        float start = 1f;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i<=count  ; i++) {
//            float mult = (range + 1);
            float val = Data.get(i).floatValue();
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

    public ArrayList<Integer> setColorForPM(ArrayList<Double>PM25){
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


    public void checkNodeInFavourite(){
        database = Database.initDatabase(this, "FeatureOfInterest.sqlite");

        Cursor cursor = database.rawQuery("SELECT FAVOURITE FROM USER_DB WHERE ID = '"+ID+"'", null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            Boolean status = Boolean.valueOf(cursor.getString(0));
            if(status == true){
                status_favourite=R.drawable.favourite;
            }
        }
        cursor.moveToFirst();
    }

    public void AddNodeToFavourite(){
        database = Database.initDatabase(this, "FeatureOfInterest.sqlite");
        Cursor cursor = database.rawQuery("SELECT * FROM USER_DB WHERE ID = '"+ID+"'", null);
        if(cursor.getCount()==0){
            cursor.close();
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID",ID);
            contentValues.put("FAVOURITE", "true");
            contentValues.put("Conditional", 0);
            database.insert("USER_DB",null, contentValues);
            cursor = database.rawQuery("SELECT * FROM USER_DB", null);
            int size = cursor.getCount();
            cursor.close();
        }else if(cursor.getCount()>0){
            cursor.close();
            ContentValues contentValues = new ContentValues();
            contentValues.put("FAVOURITE", "true");
            database.update("USER_DB",contentValues, "ID=?", new String[]{ID});
            cursor = database.rawQuery("SELECT FAVOURITE FROM USER_DB", null);
            int size = cursor.getCount();
            cursor.moveToFirst();
            Boolean status = Boolean.valueOf(cursor.getString(0));
            if(status == true){
                status_favourite=R.drawable.favourite;
            }
            cursor.close();
        }
        database.close();

//        int id = cursor.getInt(0);
    }

    public void RemoveNodeFromFavourite(){
        database = Database.initDatabase(this, "FeatureOfInterest.sqlite");
        Cursor cursor = database.rawQuery("SELECT Conditional FROM USER_DB WHERE ID = '"+ID+"'", null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            int conditional = cursor.getInt(0);
            if(conditional==0){
                database.delete("USER_DB","ID = ?", new String[]{String.valueOf(ID)});
            }else{
                ContentValues contentValues = new ContentValues();
                contentValues.put("FAVOURITE", "false");
                database.update("USER_DB", contentValues, "ID = ?", new String[]{ID});
            }
        }


    }

    public void getDataFromID(){
        database = Database.initDatabase(this, "FeatureOfInterest.sqlite");
        String query = "SELECT Temperature, Humidity FROM Data WHERE ID = "+ID;
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        Temp = cursor.getInt(0);
        Humd = cursor.getInt(1);
    }

    public void setPMColor(){
        if(PM>=0 && PM<36){
            pm_color = Color.parseColor("#4FD0FA");
        }else if(PM>35 && PM<76){
            pm_color = (Color.parseColor("#8BC441"));
        }else if(PM>75 && PM<116) {
            pm_color = (Color.parseColor("#E0C303"));
        }else if(PM>115 && PM<151) {
            pm_color = (Color.parseColor("#FB8C04"));
        }else if(PM>150 && PM<251) {
            pm_color = (Color.parseColor("#E63930"));
        }else if(PM>250 && PM<351) {
            pm_color = (Color.parseColor("#614C84"));
        }else if(PM>350 && PM<1000) {
            pm_color = (Color.parseColor("#3C3C30"));
        }
    }

    public int getConditional(){
        int conditional = 0;
        database = Database.initDatabase(this, "FeatureOfInterest.sqlite");
        String query = "SELECT Conditional FROM USER_DB WHERE ID = '"+ID+"'";
        Cursor cursor = database.rawQuery(query,null);
        int size = cursor.getCount();
        if(size>0){
            cursor.moveToFirst();
            conditional = cursor.getInt(0);
        }
        return conditional;
    }

    public ArrayList<Double> ParseData(String data){
        ArrayList<Double> data_double = new ArrayList<>();
        String[] data_array = data.split("\n");
        for (int i=0; i<data_array.length;i++){
            data_double.add(Double.parseDouble(data_array[i]));
        }
        return data_double;
    }
}


