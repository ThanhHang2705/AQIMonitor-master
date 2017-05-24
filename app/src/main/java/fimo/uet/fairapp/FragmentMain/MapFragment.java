package fimo.uet.fairapp.FragmentMain;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;


import java.util.ArrayList;
import java.util.List;

import fimo.uet.fairapp.Activities.LoaderUI;
import fimo.uet.fairapp.Adapters.KQuaAdapter;
import fimo.uet.fairapp.DatabaseManager.GetDataOfFairBox;
import fimo.uet.fairapp.KQNode;
import fimo.uet.fairapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment{

    private GoogleMap map;
    @SuppressWarnings("unused")
    private View myContentsView;
    @SuppressWarnings("unused")
    Marker my_marker;
    private String PM;
    MapView mMapView;
    AutoCompleteTextView address;
    public View v;
    private static String[] COUNTRIES = new String[]{
            "Belgium", "France", "Italy", "Germany", "Spain"
    };
    ArrayList<KQNode>listLove;
    ListView listView;
    ScrollView scrollView;
    KQuaAdapter arrayAdapter;
    private static ArrayList<String> COUNTRIES2;
    private ArrayList<KQNode> ListNode;
    private List<Marker> AllMarker;
    private ArrayList<String>PID;
    private ArrayList<MyCluster> ManagerCluster = new ArrayList<>();
    static boolean ShowMarkerInCluster = false;

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
        scrollView = (ScrollView)v.findViewById(R.id.scrollviewmapfragment);
        arrayAdapter = new KQuaAdapter(ListNode,getContext());
        AllMarker = new ArrayList<>();
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) listView.getLayoutParams();
        UpdatePID();
        lp.height = Resources.getSystem().getDisplayMetrics().heightPixels;
        listView.setLayoutParams(lp);
        listView.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                Bundle bundle = new Bundle();
                bundle.putInt("CurrentFragment", 0);
                int a = PID.size();
                bundle.putString("ID", ListNode.get(position).getID());
                bundle.putString("Address", ListNode.get(position).getAddress());
                Intent intent = new Intent(getContext(), LoaderUI.class);
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
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.INTERNET}, 1);

                    return;
                }
                map.setMyLocationEnabled(true);

                map.getUiSettings().setZoomControlsEnabled(true);
                UpdateListNode();
                my_marker = null;
                GroupMarker();
                for(int i=0;i<ManagerCluster.size();i++){
//                    Double a = ListNode.get(i).getPM();
//                    int PM = a.intValue();
                    LatLng latLng = ManagerCluster.get(i).GetLatLng();
                    int size = ManagerCluster.get(i).size();
                    my_marker = map.addMarker(new MarkerOptions()
                            .position(latLng)
//                            .title(String.valueOf(size))
                            .icon(BitmapDescriptorFactory.fromBitmap(DrawMarker(String.valueOf(size), R.drawable.group_marker, Color.BLACK)))
                            .snippet(String.valueOf(size)));
                    my_marker.setTag("cluster");
                    my_marker.setTitle(ManagerCluster.get(i).GetListID());
                    AllMarker.add(my_marker);
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(21.037600, 105.781469),15));
                }




                map.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
                    @Override
                    public void onCameraMoveCanceled() {
                        CameraPosition camera_position = map.getCameraPosition();
                        LatLng position = camera_position.target;
                        double latitude = position.latitude;
                        double longtitude = position.longitude;
                        //map.animateCamera(CameraUpdateFactory.newCameraPosition(camera_position));
                    }


                });

                map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                    @Override
                    public void onCameraMoveStarted(int i) {
                        CameraPosition camera_position = map.getCameraPosition();
                        LatLng position = camera_position.target;
                        double latitude = position.latitude;
                        double longtitude = position.longitude;
                        float distance = ComputeRadius(map);
                        float zoom = camera_position.zoom;
                        if(zoom<=19){
                            for (int j =0;j<ManagerCluster.size();j++){
                                ManagerCluster.get(j).HideListMarker();
                                AllMarker.get(j).setVisible(true);
                            }
                            ShowMarkerInCluster = false;
                        }
                        if(zoom>19 && ShowMarkerInCluster==false){
                            ShowMarkerInCluster = true;
                            GroupMarker();
                            for (int j =0;j<ManagerCluster.size();j++){
                                ManagerCluster.get(j).ShowListMarker();
                                LatLng latLng = ManagerCluster.get(j).GetLatLng();
                                int size = ManagerCluster.get(j).size();
                                AllMarker.get(j).setVisible(false);
                            }
                        }
                    }
                });

                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){


                    @Override
                    public boolean onMarkerClick(final Marker marker) {
                        if(marker.getTag().equals("FairBox")){
                            Bundle bundle = new Bundle();
                            String ID = marker.getTitle().toString();
                            for (int i = 0; i < ListNode.size(); i++) {
                                String id = ListNode.get(i).getID();
                                if (ID.equals(id)) {
                                    bundle.putString("ID", ID);
                                    bundle.putString("Address", ListNode.get(i).getAddress());
                                    break;
                                }
                            }
                            bundle.putString("PM", marker.getSnippet());
                            bundle.putInt("CurrentFragment",0);
                            Intent intent = new Intent(getContext(), LoaderUI.class);
                            intent.putExtra("TapTin", bundle);
                            startActivity(intent);
                        }else{
                            boolean a = marker.isVisible();

                            ShowMarkerInCluster = true;
                            GroupMarker();
                            for (int j =0;j<ManagerCluster.size();j++){
                                if (CompareLocation(ManagerCluster.get(j).GetLatLng(),marker.getPosition())){
//                                    new (ManagerCluster.get(j).UpdateData(getContext()).ex;
                                    ManagerCluster.get(j).ShowListMarker();
                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),21));
                                    AllMarker.get(j).setVisible(false);
                                }
                            }


                        }

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
                scrollView.setVisibility(View.VISIBLE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                listView.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
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
        GetDataOfFairBox getDataOfFairBox = new GetDataOfFairBox(getContext());
        ListNode = getDataOfFairBox.getData();
    }

    public void UpdatePID(){
        for(int i=0;i<ListNode.size();i++){
            PID = new ArrayList<>();
            PID.add(ListNode.get(i).getID());
        }
    }

    //Cập nhật dữ liệu vào mảng COUNTRIES2
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


    public void ShowAllMarker(){
        for (int i = 0;i<AllMarker.size();i++){
            AllMarker.get(i).setVisible(true);
        }
    }

    public float ComputeRadius(GoogleMap googleMap){
        VisibleRegion visibleRegion = googleMap.getProjection().getVisibleRegion();

        LatLng farRight = visibleRegion.farRight;
        LatLng farLeft = visibleRegion.farLeft;
        LatLng nearRight = visibleRegion.nearRight;
        LatLng nearLeft = visibleRegion.nearLeft;

        float[] distanceWidth = new float[2];
        Location.distanceBetween(
                (farRight.latitude+nearRight.latitude)/2,
                (farRight.longitude+nearRight.longitude)/2,
                (farLeft.latitude+nearLeft.latitude)/2,
                (farLeft.longitude+nearLeft.longitude)/2,
                distanceWidth
        );


        float[] distanceHeight = new float[2];
        Location.distanceBetween(
                (farRight.latitude+nearRight.latitude)/2,
                (farRight.longitude+nearRight.longitude)/2,
                (farLeft.latitude+nearLeft.latitude)/2,
                (farLeft.longitude+nearLeft.longitude)/2,
                distanceHeight
        );

        float distance;

        if (distanceWidth[0]>distanceHeight[0]){
            distance = distanceWidth[0];
        } else {
            distance = distanceHeight[0];
        }
        return distance;
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

    public void GroupMarker(){
        ManagerCluster.clear();
        for (int i=0;i<ListNode.size();i++){
            double latitude = ListNode.get(i).getLatLng().latitude;
            double longtitude = ListNode.get(i).getLatLng().longitude;
//            mClusterManager.addItem(new MyItem(latitude, longtitude));
            if (i==0){
                MyCluster myCluster = new MyCluster(map, getResources(),getContext());
                String title = ListNode.get(i).getID();
                Double a = ListNode.get(i).getPM();
                String snippet = String.valueOf(a.intValue());
                MyItem myItem = new MyItem(latitude,longtitude,title,snippet);
                myCluster.Add(myItem);
                ManagerCluster.add(myCluster);
            }else{
                for (int j=0;j<ManagerCluster.size();j++){
                    if(CompareLocation(ListNode.get(i).getLatLng(), ManagerCluster.get(j).GetLatLng())){
                        String title = ListNode.get(i).getID();
                        Double a = ListNode.get(i).getPM();
                        String snippet = String.valueOf(a.intValue());
                        MyItem myItem = new MyItem(latitude,longtitude,title,snippet);
                        ManagerCluster.get(j).Add(myItem);
                        break;
                    }else{
                        MyCluster myCluster = new MyCluster(map, getResources(), getContext());
                        String title = ListNode.get(i).getID();
                        Double a = ListNode.get(i).getPM();
                        String snippet = String.valueOf(a.intValue());
                        MyItem myItem = new MyItem(latitude,longtitude,title,snippet);
                        myCluster.Add(myItem);
                        ManagerCluster.add(myCluster);
                        break;
                    }
                }
            }

        }
    }

//    public String CreateListID(MyCluster.get)
}

