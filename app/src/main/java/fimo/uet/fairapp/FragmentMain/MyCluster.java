package fimo.uet.fairapp.FragmentMain;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fimo.uet.fairapp.Activities.LoaderUI;
import fimo.uet.fairapp.Data.UpdateDataForMap;
import fimo.uet.fairapp.DatabaseManager.Info_Fair_Box_DB;
import fimo.uet.fairapp.R;

/**
 * Created by HP on 5/10/2017.
 */

public class MyCluster {
    ArrayList<MyItem> TempCluster;
    ArrayList<MyItem>cluster;
    private List<Marker> AllMarker;
    static GoogleMap googleMap;
    static Resources resources;

    Context context;
    public MyCluster(GoogleMap map, Resources resources, Context context){
        this.context = context;
        cluster = new ArrayList<>();
        TempCluster = new ArrayList<>();
        AllMarker = new ArrayList<>();
        this.googleMap = map;
        this.resources = resources;
    }

    public String GetListID(){
        String array_id="";
        for (int i=0;i<cluster.size();i++){
            if(i==cluster.size()-1){
                array_id+=cluster.get(i).getTitle();
            }else{
                array_id+=cluster.get(i).getTitle()+"\n";
            }
        }
        return array_id;
    }
    public void Add(MyItem myItem){
        cluster.add(myItem);
    }

    public LatLng GetLatLng(){
        LatLng latLng = null;
        if (cluster.size()>0){
            latLng = cluster.get(0).getPosition();
        }
        return latLng;
    }

    public int size(){
        return cluster.size();
    }

    public void ShowListMarker(){
//        RemoveMarkers();
        CreateMarker();
        for (int i=0;i<AllMarker.size();i++){
            AllMarker.get(i).setVisible(true);
        }

    }

    public void HideListMarker(){
//        CreateMarker();
        RemoveMarkers();
    }

    public void RemoveMarkers(){
        for (int i=0;i<AllMarker.size();i++){
            AllMarker.get(i).remove();
        }
    }
    public void CreateMarker(){
//        RemoveMarkers();
        AllMarker.clear();
        CustomLocation();

        for (int i =0 ;i<TempCluster.size();i++){
            Marker marker;
            Double a = Double.valueOf(TempCluster.get(i).getSnippet());
            int PM = a.intValue();
            LatLng latLng = TempCluster.get(i).getPosition();
            String title = TempCluster.get(i).getTitle();
            String snippet = TempCluster.get(i).getSnippet();
            if(PM<0){
                marker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        .icon(BitmapDescriptorFactory.fromBitmap(DrawMarker("  ", R.mipmap.tot, Color.BLACK, this.resources)))
                        .snippet("  "));
                marker.setVisible(false);
                marker.setTag("FairBox");
                AllMarker.add(marker);
            } else if(PM>=0 && PM<=35){
                marker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        .icon(BitmapDescriptorFactory.fromBitmap(DrawMarker(String.valueOf(PM), R.mipmap.tot, Color.BLACK, this.resources)))
                        .snippet(String.valueOf(PM)));
                marker.setVisible(false);
                marker.setTag("FairBox");
                AllMarker.add(marker);
            }else if(PM>35 && PM<=75){
                marker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        .icon(BitmapDescriptorFactory.fromBitmap(DrawMarker(String.valueOf(PM), R.mipmap.binhthuong, Color.BLACK, this.resources)))
                        .snippet(String.valueOf(PM)));
                marker.setVisible(false);
                marker.setTag("FairBox");
                AllMarker.add(marker);
            }else if(PM>75 && PM<=115){
                marker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        .icon(BitmapDescriptorFactory.fromBitmap(DrawMarker(String.valueOf(PM), R.mipmap.canhbao, Color.BLACK, this.resources)))
                        .snippet(String.valueOf(PM)));
                marker.setVisible(false);
                marker.setTag("FairBox");
                AllMarker.add(marker);
            }else if(PM>115 && PM<=150){
                marker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        .icon(BitmapDescriptorFactory.fromBitmap(DrawMarker(String.valueOf(PM), R.mipmap.o_nhiem, Color.BLACK, this.resources)))
                        .snippet(String.valueOf(PM)));
                marker.setVisible(false);
                marker.setTag("FairBox");
                AllMarker.add(marker);
            }else if(PM>150 && PM<=250) {
                marker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        .icon(BitmapDescriptorFactory.fromBitmap(DrawMarker(String.valueOf(PM), R.mipmap.rat_o_nhiem, Color.BLACK, this.resources)))
                        .snippet(String.valueOf(PM)));
                marker.setVisible(false);
                marker.setTag("FairBox");
                AllMarker.add(marker);
            }else if(PM>250 && PM<=350) {
                marker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        .icon(BitmapDescriptorFactory.fromBitmap(DrawMarker(String.valueOf(PM), R.mipmap.vo_cung_o_nhiem, Color.BLACK, this.resources)))
                        .snippet(String.valueOf(PM)));
                marker.setTag("FairBox");
                marker.setVisible(false);
                AllMarker.add(marker);
            }else if(PM>350 && PM<=999) {
                marker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        .icon(BitmapDescriptorFactory.fromBitmap(DrawMarker(String.valueOf(PM), R.mipmap.khong_the_song, Color.BLACK, this.resources)))
                        .snippet(String.valueOf(PM)));
                marker.setVisible(false);
                marker.setTag("FairBox");
                AllMarker.add(marker);
            }
         }

         googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
             @Override
             public boolean onMarkerClick(Marker marker) {
                 String snippet = marker.getSnippet().toString();
                 if (marker!=null){
                     if(!marker.getTag().equals("cluster")){
                         googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),21));
                         Bundle bundle = new Bundle();
                         String ID = marker.getTitle().toString();
                         bundle.putString("ID", ID);
                         bundle.putString("Address", GetAddress(ID));
                         bundle.putString("PM", marker.getSnippet());
                         bundle.putInt("CurrentFragment",0);
                         Intent intent = new Intent(context, LoaderUI.class);
                         intent.putExtra("TapTin", bundle);
                         context.startActivity(intent);
                     }else{
                        new UpdateData(context).execute();
                         googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),21));
                     }
                 }
                 return false;
             }
         });

    }

    public Bitmap DrawMarker(String number,int idImage, int color, Resources resources){
        Bitmap bm = BitmapFactory.decodeResource(resources, idImage);
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
        paint.setTextSize((int) (17 * scale));
        Rect bounds = new Rect();
        String gText = number;
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (width - bounds.width())/2;
        int y = (height + bounds.height())/2;

        c.drawText(gText, x, y, paint);
        /*Toast.makeText(getContext(),Float.toString(scale),Toast.LENGTH_LONG).show();*/
        return newImage;
    }

    public void CustomLocation(){
        TempCluster.clear();
        TempCluster = cluster;
        for (int i=0;i<TempCluster.size();i++){
            double offset = 0.00004;
            if (i==1){
                double lat = GetLatLng().latitude + offset;
                double lng = GetLatLng().longitude ;
                TempCluster.get(i).setLatLng(lat,lng);
            }else if (i==2){
                double lat = GetLatLng().latitude - offset;
                double lng = GetLatLng().longitude ;
                TempCluster.get(i).setLatLng(lat,lng);
            }else if(i==3){
                double lat = GetLatLng().latitude ;
                double lng = GetLatLng().longitude + offset;
                TempCluster.get(i).setLatLng(lat,lng);
            }else if (i==4){
                double lat = GetLatLng().latitude ;
                double lng = GetLatLng().longitude - offset;
                TempCluster.get(i).setLatLng(lat,lng);
            }else if (i==5){
                double lat = GetLatLng().latitude + offset;
                double lng = GetLatLng().longitude + offset;
                TempCluster.get(i).setLatLng(lat,lng);
            }else if (i==6){
                double lat = GetLatLng().latitude + offset;
                double lng = GetLatLng().longitude - offset;
                TempCluster.get(i).setLatLng(lat,lng);
            }else if (i==7){
                double lat = GetLatLng().latitude - offset;
                double lng = GetLatLng().longitude + offset;
                TempCluster.get(i).setLatLng(lat,lng);
            }else if (i==8){
                double lat = GetLatLng().latitude - offset;
                double lng = GetLatLng().longitude - offset;
                TempCluster.get(i).setLatLng(lat,lng);
            }
        }
    }

    public String GetAddress(String pID){
        String address = null;
        Info_Fair_Box_DB info_fair_box_db = new Info_Fair_Box_DB(context);
        address = info_fair_box_db.GetAddressFromID(pID);
        return address;
    }

    public boolean CompareLocation(LatLng latLng1, LatLng latLng2){
        boolean result = false;
        Location Location1 = new Location("");
        Location1.setLatitude(latLng1.latitude);
        Location1.setLongitude(latLng1.longitude);
        Location Location2 = new Location("");
        Location2.setLatitude(latLng2.latitude);
        Location2.setLongitude(latLng2.longitude);
        double distance = Location1.distanceTo(Location2);
        if(distance>1.0){
            result = false;
        }else{
            result = true;
        }
        return result;
    }

    public void SetSnipetForMarker(){
        for (int i=0;i<cluster.size();i++){
            SQLiteDatabase database =context.openOrCreateDatabase("FeatureOfInterest.sqlite", context.MODE_PRIVATE,null);
            String ID = cluster.get(i).getTitle();
            Cursor cursor = database.rawQuery("SELECT * FROM INDEX_FAIR_BOX WHERE ID = '"+ID+"'",null);
            if (cursor.getCount()>0){
                cursor.moveToFirst();
                int PM = (int) Math.round(cursor.getDouble(i));
                cluster.get(i).setTitle(String.valueOf(PM));
            }

        }
        RemoveMarkers();

    }

    class UpdateData extends AsyncTask<String,String,String>{
        Context context;
        ProgressDialog dialog;
        public UpdateData(Context context){
            this.context = context;
            dialog = new ProgressDialog(context);
        }

        @Override
        protected String doInBackground(String... params) {
            new Thread(new Runnable() {
                @Override
                public void run() {
//                LoadDataForMap_Progress loadDataForMap_progress =
//                        new LoadDataForMap_Progress(context,GetListID());
//                loadDataForMap_progress.execute();
                    UpdateDataForMap updateDataForMap = new UpdateDataForMap(context,GetListID());
                    updateDataForMap.getData();
                }
            }).start();
            int interval = 0;
            Calendar calendar = Calendar.getInstance();
            long now = calendar.getTimeInMillis();
            long timestamp = now / 1000;
            while (interval<12){
                calendar = Calendar.getInstance();
                now = calendar.getTimeInMillis();
                long current_timestamp = now / 1000;
                interval = (int) (current_timestamp-timestamp);
            }
            SetSnipetForMarker();
            return null;
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage(context.getString(R.string.device_connecting));
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
//
        }
    }
}
