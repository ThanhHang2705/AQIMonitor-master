package fimo.uet.fairapp.MyNotification;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by HP on 4/22/2017.
 */
public class NodeInfo{
    String ID;
    String NameNode;
    String Address;
    LatLng latLng;
    public NodeInfo(String ID, String NameNode, String Address, double Latitude, double Longtitude){
        this.ID = ID;
        this.NameNode = NameNode;
        this.Address = Address;
        this.latLng = new LatLng(Latitude,Longtitude);
    }

    public String getID(){return ID;}
    public String getNameNode(){return NameNode;}

    public String getAddress() {
        return Address;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
