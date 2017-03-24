package com.example.thanhhang.mnsfimo.FragmentMain;


import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanhhang.mnsfimo.Activities.Detail;
import com.example.thanhhang.mnsfimo.Adapters.KQuaAdapter;
import com.example.thanhhang.mnsfimo.Data.DataFromLocalHost;
import com.example.thanhhang.mnsfimo.Data.Database;
import com.example.thanhhang.mnsfimo.KQNode;
import com.example.thanhhang.mnsfimo.MainActivity;
import com.example.thanhhang.mnsfimo.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    private GoogleMap map;
    @SuppressWarnings("unused")
    private View myContentsView;
    @SuppressWarnings("unused")
    Marker marker;
    private String PM;
    MapView mMapView;
    AutoCompleteTextView address;
    public View v;
    private static String[] COUNTRIES = new String[]{
            "Belgium", "France", "Italy", "Germany", "Spain"
    };
    ArrayList<KQNode>listLove;
    ListView listView;
    KQuaAdapter arrayAdapter;
    private static ArrayList<String> COUNTRIES2;
    private ArrayList<KQNode> ListNode;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        v = inflater.inflate(R.layout.fragment_map, container, false);
        UpdateListNode();
        UpdateListNameNode();
        listView = (ListView) v.findViewById(R.id.result_4_basic_search);
        arrayAdapter = new KQuaAdapter(ListNode,getContext());
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) listView.getLayoutParams();

        lp.height = Resources.getSystem().getDisplayMetrics().heightPixels;
        listView.setLayoutParams(lp);
        listView.setVisibility(View.GONE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("PM", Integer.parseInt(ListNode.get(position).getPM()));
                bundle.putString("Address", ListNode.get(position).getNameNode());
                Intent intent = new Intent(getContext(), Detail.class);
                intent.putExtra("TapTin", bundle);
                startActivity(intent);
            }
        });

        mMapView = (MapView) v.findViewById(R.id.map);


        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();
        mMapView.getMapAsync(new OnMapReadyCallback() {
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                map.setMyLocationEnabled(true);
                UpdateListNode();
                for(int i=0;i<ListNode.size();i++){
                    int PM = Integer.parseInt(ListNode.get(i).getPM());
                    if(PM<=50) {
                        map.addMarker(new MarkerOptions()
                                .position(ListNode.get(i).getLatLng())
                                .title(ListNode.get(i).getNameNode())
                                .icon(BitmapDescriptorFactory.fromBitmap(DrawMarker(ListNode.get(i).getPM(), R.mipmap.green_square2,Color.YELLOW)))
                                .snippet(ListNode.get(i).getPM()));
                    }else if(PM>50 && PM<=100){
                        map.addMarker(new MarkerOptions()
                                .position(ListNode.get(i).getLatLng())
                                .title(ListNode.get(i).getNameNode())
                                .icon(BitmapDescriptorFactory.fromBitmap(DrawMarker(ListNode.get(i).getPM(), R.mipmap.yellow_square2, Color.BLACK)))
                                .snippet(ListNode.get(i).getPM()));
                    }else if(PM>100 && PM<=150){
                        map.addMarker(new MarkerOptions()
                                .position(ListNode.get(i).getLatLng())
                                .title(ListNode.get(i).getNameNode())
                                .icon(BitmapDescriptorFactory.fromBitmap(DrawMarker(ListNode.get(i).getPM(), R.mipmap.orange_square2, Color.BLACK)))
                                .snippet(ListNode.get(i).getPM()));
                    }else if(PM>150 && PM<=200){
                        map.addMarker(new MarkerOptions()
                                .position(ListNode.get(i).getLatLng())
                                .title(ListNode.get(i).getNameNode())
                                .icon(BitmapDescriptorFactory.fromBitmap(DrawMarker(ListNode.get(i).getPM(), R.mipmap.red_square2, Color.WHITE)))
                                .snippet(ListNode.get(i).getPM()));
                    }

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(ListNode.get(i).getLatLng(),10));
                }

                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){


                    @Override
                    public boolean onMarkerClick(final Marker marker) {

                        DataFromLocalHost dataFromLocalHost = new DataFromLocalHost();
                        dataFromLocalHost.execute();
                        String AllData = null;
                        try {
                            AllData = dataFromLocalHost.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        final AlertDialog.Builder alertadd = new AlertDialog.Builder(getContext());
                        LayoutInflater factory = LayoutInflater.from(getContext());
                        final View view = factory.inflate(R.layout.location_info, null);
                        TextView tvTitle = (TextView)view.findViewById(R.id.title);
                        tvTitle.setText(marker.getTitle());
                        final AlertDialog alertDialog = alertadd.create();

                        ImageView imgView = ((ImageView)view.findViewById(R.id.img));
                        int PM = Integer.parseInt(marker.getSnippet());
                        if(PM<=50) {
                            imgView.setImageBitmap(DrawMarker(marker.getSnippet(), R.mipmap.green, Color.YELLOW));
                        }else if(PM>50 && PM<=100){
                            imgView.setImageBitmap(DrawMarker(marker.getSnippet(), R.mipmap.yellow, Color.BLACK));
                        }
                        else if(PM>100 && PM<=150){
                            imgView.setImageBitmap(DrawMarker(marker.getSnippet(), R.mipmap.orange, Color.BLACK));
                        }
                        else if(PM>150 && PM<=200){
                            imgView.setImageBitmap(DrawMarker(marker.getSnippet(), R.mipmap.red, Color.WHITE));
                        }
                        @SuppressWarnings("unused") final
                        Button detail = (Button)view.findViewById(R.id.detail);
                        final String finalAllData = AllData;
                        detail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(),"detail",Toast.LENGTH_LONG).show();
                                Bundle bundle = new Bundle();
                                bundle.putString("AllData", finalAllData);
                                String NameNode=marker.getTitle().toString();
                                for (int i=0 ;i<ListNode.size();i++){
                                    String nameNode = ListNode.get(i).getNameNode();
                                    if(NameNode.equals(nameNode)){
                                        //                                Toast.makeText(getContext(),temp.get(i).getID(),Toast.LENGTH_LONG).show();
                                        bundle.putInt("ID",ListNode.get(i).getID());
                                        break;
                                    }
                                }
//                                bundle.putInt("PM", Integer.parseInt(marker.getSnippet()));
//                                bundle.putString("Address", marker.getTitle());
                                Intent intent = new Intent(getContext(),Detail.class);
                                intent.putExtra("TapTin", bundle);
                                startActivity(intent);
                            }
                        });
                        @SuppressWarnings("unused")
                        Button follow = (Button)view.findViewById(R.id.follow);
                        follow.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                                ViewPager pager = ((MainActivity)getActivity()).pager;
                                /*Toast.makeText(getContext(),"follow",Toast.LENGTH_LONG).show();*/
                                pager.getAdapter().notifyDataSetChanged();
                                pager.setCurrentItem(1);
                                UpdateListLove();
                                if(listLove.size()==0){
                                    KQNode kqNode = new KQNode(marker.getTitle().toString(), marker.getSnippet(),23,43,marker.getPosition());
//                                Love love = new Love(marker.getTitle().toString(), Integer.parseInt(marker.getSnippet()),23,43,marker.getPosition());
                                    InsertDataToFavouriteListSQLite(marker.getTitle().toString());

                                }else{
                                    String NameNode = marker.getTitle().toString();
                                    for(int i=0; i<listLove.size();i++){
                                        String NameNodeToCompare = listLove.get(i).getNameNode();
                                        if(NameNode.equals(NameNodeToCompare)){
                                            break;
                                        }else if(i==listLove.size()-1){
                                            KQNode kqNode = new KQNode(marker.getTitle().toString(), marker.getSnippet(),23,43,marker.getPosition());
//                                Love love = new Love(marker.getTitle().toString(), Integer.parseInt(marker.getSnippet()),23,43,marker.getPosition());
                                            InsertDataToFavouriteListSQLite(marker.getTitle().toString());
                                            break;
                                        }
                                    }
                                }
                                ArrayList<Fragment> al = (ArrayList<Fragment>) getFragmentManager().getFragments();

                                Fragment removeFragment = al.get(1);
                                if(removeFragment != null) {
                                    getFragmentManager().beginTransaction().remove(removeFragment);
                                }
                                FragmentManager FM = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = FM.beginTransaction();


                                //fragmentTransaction.remove(getFragmentManager().findFragmentById(R.id.fragment_listmain));
                                //getFragmentManager().executePendingTransactions();
                                ListmainFragment fragment1 = new ListmainFragment();
//                                fragment1.UpdateListLove();
//                                fragment1.UpdateAdapter();
                                fragmentTransaction.replace(R.id.fragment_listmain,fragment1);
                                fragmentTransaction.detach(fragment1);
                                fragmentTransaction.attach(fragment1);
                                fragmentTransaction.commit();



                            }
                        });
                        alertDialog.setView(view);
                        alertDialog.show();

                        return false;
                    }
                });

            }
        });

        return v;
    }


    // Hiển thị thanh tìm kiếm cơ bản trên ActionBar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.basic_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setOnQueryTextListener(this);
//        searchView.setQueryHint("Search");
        searchView.setOnSearchClickListener(new SearchView.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.VISIBLE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                listView.setVisibility(View.GONE);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    //Đưa dữ liệu từ List_Favourite.sqlite vào mảng
    public void UpdateListNode(){
        ListNode = new ArrayList<>();
        ListNode = ((MainActivity)getActivity()).getDataFromSQLite();
    }

    //Cập nhật dữ liệu vào mảng COUNTRIES2
    public void UpdateListNameNode(){
        COUNTRIES2 = new ArrayList<>();
        for (int i=0;i<ListNode.size();i++){
            COUNTRIES2.add(ListNode.get(i).getNameNode());
        }
    }

    public void UpdateListLove(){

        listLove = ((MainActivity)getActivity()).getFavouriteList();
    }
    //Hàm thêm Node vào trong danh sách yêu thích được lưu ở file List_Favourite.sqlite
    public void InsertDataToFavouriteListSQLite(String NameNode){
        int ID = 0;
        for(int i=0;i<ListNode.size();i++){
            String nameNode = ListNode.get(i).getNameNode();
            if(NameNode.equals(nameNode)){
                ID = ListNode.get(i).getID();
                break;
            }
        }

        SQLiteDatabase database = Database.initDatabase(getActivity(),"List_Favourite.sqlite");
        Cursor cursor = database.rawQuery("SELECT * FROM Favourite",null);
        cursor.getCount();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", ID);
        database.insert("Favourite",null, contentValues);
        cursor = database.rawQuery("SELECT * FROM Favourite", null);
        cursor.getCount();
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    //Hàm để vẽ lại các Node trên Map thay cho biểu tượng picker của bản đồ
    @SuppressWarnings("unused")
    public Bitmap DrawMarker(String number,int idImage, int color){
        Bitmap bm = BitmapFactory.decodeResource(getResources(), idImage);
        /*Bitmap bm = BitmapFactory.decodeR*/

        Bitmap.Config config = bm.getConfig();
        int width = bm.getWidth();
        int height = bm.getHeight();

        Bitmap newImage = Bitmap.createBitmap(width, height, config);

        Canvas c = new Canvas(newImage);
        c.drawBitmap(bm, 0, 0, null);

        Paint paint = new Paint();

        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
                /*paint.setTextSize(40);*/
        /*float scale = getResources().getDisplayMetrics().scaledDensity;*/
        float scale =width/35;
        paint.setTextSize((int) (20 * scale));
        Rect bounds = new Rect();
        String gText = number;
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (width - bounds.width())/2;
        int y = (height + bounds.height())/2;

        c.drawText(gText, x, y, paint);
        /*Toast.makeText(getContext(),Float.toString(scale),Toast.LENGTH_LONG).show();*/
        return newImage;
    }




}

