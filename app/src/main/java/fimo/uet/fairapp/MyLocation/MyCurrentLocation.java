package fimo.uet.fairapp.MyLocation;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

import fimo.uet.fairapp.Activities.AddnodeActivity;

/**
 * Created by HP on 4/19/2017.
 */

public class MyCurrentLocation extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    TextView latitudeText;
    TextView longitudeText;
    private FusedLocationProviderApi locationProvider = LocationServices.FusedLocationApi;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Double myLatitude=0.0;
    private Double myLongitude=0.0;
    private String Address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(15 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


    }

    public double getLatitude(){return myLatitude;};
    public double getLongtitude(){return myLongitude;}

    @Override
    public void onConnected(Bundle bundle) {
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();
        Address = getAddressFromLatLng();
        Intent intent = new Intent(this,AddnodeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Address", Address);
        bundle.putDouble("Latitude",getLatitude());
        bundle.putDouble("Longtitude", getLongtitude());
        intent.putExtra("LatLng",bundle);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()) {
            requestLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    public String getAddressFromLatLng(){
        String strAdd = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {

            List<Address> addresses = geocoder.getFromLocation(myLatitude, myLongitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(", ");
//                    strAdd.add(returnedAddress.getAddressLine(i));
                }
                strAdd=strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    class ProgressTask extends AsyncTask<String, String, String>{
        Activity activity;
        ProgressDialog progressDialog;
        public ProgressTask(Activity activity){
            this.activity = activity;
            progressDialog = new ProgressDialog(activity);
        }
        @Override
        protected void onPreExecute() {
            this.progressDialog.setMessage("Đang tìm kiếm...");
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String Address = null;
            if(myLatitude!=0 || myLongitude!=0){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Address = getAddressFromLatLng();
            return Address;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s!=null){
                progressDialog.dismiss();
                Intent intent = new Intent(activity,AddnodeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Address", s);
                bundle.putDouble("Latitude",getLatitude());
                bundle.putDouble("Longtitude", getLongtitude());
                intent.putExtra("LatLng",bundle);
                startActivity(intent);
                activity.finish();
            }
        }
    }
}