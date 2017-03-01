package com.example.thanhhang.mnsfimo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thanhhang.mnsfimo.Activities.AddnodeActivity;
import com.example.thanhhang.mnsfimo.Activities.IntroduceActivity;
import com.example.thanhhang.mnsfimo.Activities.SettingActivity;
import com.example.thanhhang.mnsfimo.Data.Database;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {
    public ViewPager pager;
    public TabLayout tabLayout;
    private static String[] COUNTRIES = new String[] {
            "Sân Bay Nội Bài", "VNU", "Thành Cổ Sơn Tây", "Chùa Hương", "Bát Tràng"
    };
//    String DataFromSQLite[];
    ListView listView;
    SQLiteDatabase database;
    private static ArrayList<KQNode> DataFromSQLite;
    private static ArrayList<KQNode> FavouriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Code viewpager
        /*
        pager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        FragmentManager manager = getSupportFragmentManager();
        PagerAdapter adapter = new PagerAdapter(manager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);
        */

//        getDataFromSQLite();
//        getFavouriteList();


        // Code khi dong mo navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_addNode) {
            startActivity(new Intent(this, AddnodeActivity.class));

        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(this, SettingActivity.class));
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "Chia se", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_Feedback) {
            sendEmail();

        } else if (id == R.id.nav_introduce) {
            startActivity(new Intent(this, IntroduceActivity.class));
        } else if (id == R.id.nav_rate) {
            Toast.makeText(this, "danh gia", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    protected void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto: thanhhang27051996@gmail.com"));
        startActivity(Intent.createChooser(emailIntent, "Send feedback to us"));
    }

    public ArrayList<KQNode> getDataFromSQLite(){
        database = Database.initDatabase(this, "pm_monitor.sqlite");
        Cursor cursor = database.rawQuery("SELECT * FROM Data_Demo", null);
        cursor.moveToFirst();
        DataFromSQLite = new ArrayList();
        for(int i=0;i<cursor.getCount();i++){
            int ID = cursor.getInt(0);
            String NameNode = cursor.getString(1);
            String Address = cursor.getString(2);
            String PM = cursor.getString(3);
            LatLng latLng = new LatLng(cursor.getDouble(4),cursor.getDouble(5));
            KQNode kqNode = new KQNode(ID,NameNode,Address,PM,latLng);
            DataFromSQLite.add(kqNode);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return DataFromSQLite;
    }


    public ArrayList<KQNode> getFavouriteList(){
        database = Database.initDatabase(this, "List_Favourite.sqlite");

        Cursor cursor = database.rawQuery("SELECT * FROM Favourite", null);
        FavouriteList = new ArrayList<>();
        int size = cursor.getCount();
        cursor.moveToFirst();
        for (int i=0;i<cursor.getCount(); i++){
            int ID = cursor.getInt(0);
            for (int j=0; j<DataFromSQLite.size(); j++){
                if (ID == DataFromSQLite.get(j).getID()){
                    String NameNode = DataFromSQLite.get(j).getNameNode();
                    String PM = DataFromSQLite.get(j).getPM();
                    int Humidity = 43;
                    int Temperature = 32;
                    LatLng latLng = DataFromSQLite.get(j).getLatLng();
                    KQNode kqNode = new KQNode(NameNode,PM,Humidity,Temperature,latLng);
                    FavouriteList.add(kqNode);
                    break;
                }
            }
            cursor.moveToNext();
        }
        return FavouriteList;
    }

}
