package com.example.thanhhang.mnsfimo.FragmentMain;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.thanhhang.mnsfimo.Activities.Detail;
import com.example.thanhhang.mnsfimo.KQNode;
import com.example.thanhhang.mnsfimo.Love;
import com.example.thanhhang.mnsfimo.MainActivity;
import com.example.thanhhang.mnsfimo.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    private GoogleMap map;
    @SuppressWarnings("unused")
    private View myContentsView;
    @SuppressWarnings("unused")
    Marker marker;
    private  String PM;
    MapView mMapView;
    AutoCompleteTextView address;
    public View v;
    private static String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };
    private  static ArrayList<String> COUNTRIES2 ;
    private ArrayList<KQNode> ListNode;
    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_map, container, false);
        UpdateListNode();
        UpdateListNameNode();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, COUNTRIES2);
        address = (AutoCompleteTextView) v.findViewById(R.id.address);
        address.setAdapter(adapter);

        /*address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TabLayout tabLayout = ((MainActivity)getActivity()).tabLayout;
                tabLayout.setVisibility(View.GONE);
            }

        });*/

        mMapView = (MapView) v.findViewById(R.id.map);


        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();
        mMapView.getMapAsync(new OnMapReadyCallback(){
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
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
               /* LatLng NoiBai = new LatLng(21.217712, 105.792383);
                PM = "40";
                map.addMarker(new MarkerOptions()
                        .position(NoiBai)
                        .title("San Bay Noi Bai")
                        .icon( BitmapDescriptorFactory.fromBitmap(DrawMarker(PM, R.mipmap.green_square2)))
                        .snippet(PM));

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(NoiBai,10));

                LatLng vnu = new LatLng(21.037442, 105.781376);
                PM="30";
                map.addMarker(new MarkerOptions()
                        .position(vnu)
                        .title("VNU")
                        .icon( BitmapDescriptorFactory.fromBitmap(DrawMarker(PM, R.mipmap.green_square2)))
                        .snippet(PM));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(vnu,10));

                LatLng SonTay = new LatLng(21.139634, 105.50423);
                PM="35";
                map.addMarker(new MarkerOptions()
                        .position(SonTay)
                        .title("Thanh Co Son Tay")
                        .icon( BitmapDescriptorFactory.fromBitmap(DrawMarker(PM,R.mipmap.green_square2)))
                        .snippet(PM));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(SonTay,10));

                LatLng ChuaHuong = new LatLng(20.616085, 105.744802);
                PM="20";
                Marker m = map.addMarker(new MarkerOptions()
                        .position(ChuaHuong).title("Chua Huong")
                        *//*.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))*//*
                        .icon( BitmapDescriptorFactory.fromBitmap(DrawMarker(PM,R.mipmap.green_square2)))
                        .snippet(PM));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(ChuaHuong,10));
                *//*m.showInfoWindow();*//*

                LatLng BatTrang = new LatLng(20.976217, 105.912747);
                PM = "30";
                map.addMarker(new MarkerOptions()
                        .position(BatTrang)
                        .title("Bat Trang")
                        .icon( BitmapDescriptorFactory.fromBitmap(DrawMarker(PM,R.mipmap.green_square2)))
                        .snippet(PM));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(BatTrang,10));*/
                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){


                    @Override
                    public boolean onMarkerClick(final Marker marker) {

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
                        detail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(),"detail",Toast.LENGTH_LONG).show();

                                Bundle bundle = new Bundle();
                                bundle.putInt("PM", Integer.parseInt(marker.getSnippet()));

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
                                FragmentManager FM = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = FM.beginTransaction();
                                Love love = new Love(marker.getTitle().toString(), Integer.parseInt(marker.getSnippet()),23,43,marker.getPosition());
                                ListmainFragment fragment1 = new ListmainFragment(love);
                                fragmentTransaction.replace(R.id.fragment_listmain,fragment1);
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

    public void UpdateListNode(){
        ListNode = new ArrayList<>();
        ListNode.add(new KQNode(1, "Sân Bay Nội Bài", "Nội Bài", "120", new LatLng(21.217712, 105.792383)));
        ListNode.add(new KQNode(2, "VNU", "123 Xuân Thủy, Cầu Giấy, Hà Nội", "151", new LatLng(21.037442, 105.781376)));
        ListNode.add(new KQNode(3, "Thành Cổ Sơn Tây", "Thị xã Sơn Tây", "86", new LatLng(21.139634, 105.50423)));
        ListNode.add(new KQNode(4, "Chùa Hương", "Hương Sơn, Mỹ Đức, Hà Nội", "40", new LatLng(20.616085, 105.744802)));
        ListNode.add(new KQNode(5, "Bát Tràng", "Bát Tràng, Gia Lâm, Hà Nội", "55", new LatLng(20.976217, 105.912747)));
    }

    public void UpdateListNameNode(){
        COUNTRIES2 = new ArrayList<>();
        for (int i=0;i<ListNode.size();i++){
            COUNTRIES2.add(ListNode.get(i).getNameNode());
        }

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
