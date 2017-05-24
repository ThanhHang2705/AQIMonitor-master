package fimo.uet.fairapp.FragmentMain;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by HP on 5/10/2017.
 */

public class MyItem {
    private LatLng mPosition;
    private String mTitle;
    private String mSnippet;

    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public MyItem(double lat, double lng, String title, String snippet) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
    }

    public void setTitle(String title){
        mTitle = title;
    }
    public void setLatLng(double latitude, double longtitude){
        mPosition = new LatLng(latitude,longtitude);
    }

    public LatLng getPosition() {
        return mPosition;
    }

    public String getTitle() {
        return mTitle;
    }


    public String getSnippet() {
        return mSnippet;
    }
}