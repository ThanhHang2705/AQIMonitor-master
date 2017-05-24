package fimo.uet.fairapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;

import fimo.uet.fairapp.Activities.IntroduceActivity;
import fimo.uet.fairapp.Activities.SettingActivity;
import fimo.uet.fairapp.Adapters.PagerAdapter;
import fimo.uet.fairapp.Data.Database;
import fimo.uet.fairapp.MyLocation.MyCurrentLocation;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
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
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        // Code viewpager

        pager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        FragmentManager manager = getSupportFragmentManager();
        PagerAdapter adapter = new PagerAdapter(manager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        setCurrentFragment();
        //pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        setupTabIcons();
//        tabLayout.setTabsFromPagerAdapter(adapter);
//        tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getIcon().setColorFilter(Color.parseColor("#4285F6"), PorterDuff.Mode.SRC_IN);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

//                        tabLayout.getTabAt(position).getIcon().setColorFilter(Color.parseColor("#2e0bd4"), PorterDuff.Mode.SRC_IN);


            }



            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                tab.getIcon().setColorFilter(Color.parseColor("#4285F6"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                tab.getIcon().setColorFilter(Color.parseColor("#757575"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

//        CreateTable();

//        showTable();
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
        if (isConnected()) {
            Toast.makeText(MainActivity.this,"You are conncted",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this,"You are NOT conncted",Toast.LENGTH_LONG).show();
        }
    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_map_black_24dp,
                R.drawable.ic_format_list_bulleted_black_24dp,
                R.drawable.ic_edit_location_black_24dp
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }



    @Override
    public void onBackPressed() {
//        startActivity(new Intent(this, MainActivity.class));
//        Intent intent = new Intent();
//        intent.putIntegerArrayListExtra(SELECTION_LIST, selected);
//        setResult(RESULT_OK, intent);
//        finish();
//        startActivity(new Intent(this, MainActivity.class));
        onPause();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        Toast.makeText(this, " resume" , Toast.LENGTH_LONG).show();

        super.onResume();
    }

    @Override
    protected void onPause() {
//        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        List< ActivityManager.RunningTaskInfo > runningTaskInfo = am.getRunningTasks(1);
//
//        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
//        String s = componentInfo.getShortClassName();
//        if(componentInfo.getShortClassName().equals(".MainActivity")){
//            startActivity(new Intent(this, MainActivity.class));
//        }
//        Toast.makeText(this, " pause" , Toast.LENGTH_LONG).show();

        super.onPause();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_addNode) {
            startActivity(new Intent(this, MyCurrentLocation.class));

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


    public void CreateTable(){

        SQLiteDatabase database = Database.initDatabase(this, "FeatureOfInterest.sqlite");
        database.execSQL("DROP TABLE Notification");
        database.execSQL("CREATE TABLE IF NOT EXISTS Notification(\n" +
                "   ID INT     NOT NULL,\n" +
                "   Conditional     NOT NULL\n" +
                ");");
    }


    public void showTable(){
        SQLiteDatabase db = Database.initDatabase(this, "FeatureOfInterest.sqlite");
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                Toast.makeText(MainActivity.this, "Table Name=> "+c.getString(0), Toast.LENGTH_SHORT).show();
                c.moveToNext();
            }
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Conect Failed", Toast.LENGTH_LONG).show();
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public void setCurrentFragment(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("TapTin");
        if (bundle != null) {
            int currentFragment = bundle.getInt("CurrentFragment");
            pager.setCurrentItem(currentFragment);
        }

    }
}
