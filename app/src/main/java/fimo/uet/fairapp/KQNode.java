package fimo.uet.fairapp;

import android.annotation.SuppressLint;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ThanhHang on 12/6/2016.
 */

public class KQNode {
    private String ID;
    private String nameNode;
    private String address;
    private double PM;
    private double Humidity;
    private double Temperature;
    LatLng latLng;

    public KQNode(String ID, String nameNode, String address, double PM, LatLng latLng) {
        this.ID = ID;
        this.nameNode = nameNode;
        this.address = address;
        this.PM = PM;
        this.latLng = latLng;
    }
    @SuppressLint("ValidFragment")
    public KQNode( String nameNode, double PM, double Temperature, double Humidity, LatLng latLng) {

        this.nameNode = nameNode;

        this.PM = PM;
        this.Temperature = Temperature;
        this.Humidity = Humidity;
        this.latLng = latLng;
    }

    public KQNode(String ID, String nameNode, String address, LatLng latLng, double PM, double temperature, double humidity) {
        this.ID = ID;
        this.nameNode = nameNode;
        this.address = address;
        this.PM = PM;
        Humidity = humidity;
        Temperature = temperature;
        this.latLng = latLng;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNameNode() {
        return nameNode;
    }

    public void setNameNode(String nameNode) {
        this.nameNode = nameNode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPM() {
        return PM;
    }

    public void setPM(double PM) {
        this.PM = PM;
    }

    public void setHumidity(double Humidity){
        this.Humidity = Humidity;
    }

    public double getHumidity(){
        return Humidity;
    }

    public void setTemperature(double Temperature){
        this.Temperature = Temperature;
    }

    public double getTemperature(){
        return Temperature;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
