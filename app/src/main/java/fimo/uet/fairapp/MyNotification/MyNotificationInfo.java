package fimo.uet.fairapp.MyNotification;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by HP on 4/22/2017.
 */

public class MyNotificationInfo {
    String ID, NameNode, Address;
    LatLng latLng;
    double PM, Temperature, Humidity;
    public MyNotificationInfo(NodeInfo nodeInfo, IndexInfo indexInfo){
        this.ID = nodeInfo.getID();
        this.NameNode = nodeInfo.getNameNode();
        this.Address = nodeInfo.getAddress();
        this.latLng = nodeInfo.getLatLng();
        this.PM = indexInfo.getPM();
        this.Temperature = indexInfo.getTemperature();
        this.Humidity = indexInfo.getHumidity();
    }

    public String getID() {
        return ID;
    }

    public String getNameNode() {
        return NameNode;
    }

    public String getAddress() {
        return Address;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public double getPM() {
        return PM;
    }

    public double getTemperature() {
        return Temperature;
    }

    public double getHumidity() {
        return Humidity;
    }
}

