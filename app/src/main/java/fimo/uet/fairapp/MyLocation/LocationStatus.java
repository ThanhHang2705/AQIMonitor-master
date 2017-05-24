package fimo.uet.fairapp.MyLocation;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

/**
 * Created by HP on 4/19/2017.
 */

public class LocationStatus {
    Context context;
    public LocationStatus(Context context){
        this.context = context;
    }

    public boolean isLocationServiceEnabled(){
        LocationManager locationManager = null;
        boolean gps_enabled= false,network_enabled = false;

        if(locationManager ==null)
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try{
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch(Exception ex){
            //do nothing...
        }

        try{
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ex){
            //do nothing...
        }

        return gps_enabled || network_enabled;

    }

    public void EnableLocation(){
        Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(myIntent);
    }
}
