package com.example.thanhhang.mnsfimo;

import android.annotation.SuppressLint;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ThanhHang on 12/6/2016.
 */

public class KQNode {
    private int ID;
    private String nameNode;
    private String address;
    private String PM;
    private int Humidity;
    private int Temperature;
    LatLng latLng;

    public KQNode(int ID, String nameNode, String address, String PM, LatLng latLng) {
        this.ID = ID;
        this.nameNode = nameNode;
        this.address = address;
        this.PM = PM;
        this.latLng = latLng;
    }
    @SuppressLint("ValidFragment")
    public KQNode( String nameNode, String PM, int Temperature, int Humidity, LatLng latLng) {

        this.nameNode = nameNode;

        this.PM = PM;
        this.Temperature = Temperature;
        this.Humidity = Humidity;
        this.latLng = latLng;
    }

    public KQNode(int ID, String nameNode, String address, String PM, int humidity, int temperature, LatLng latLng) {
        this.ID = ID;
        this.nameNode = nameNode;
        this.address = address;
        this.PM = PM;
        Humidity = humidity;
        Temperature = temperature;
        this.latLng = latLng;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
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

    public String getPM() {
        return PM;
    }

    public void setPM(String PM) {
        this.PM = PM;
    }

    public void setHumidity(int Humidity){
        this.Humidity = Humidity;
    }

    public int getHumidity(){
        return Humidity;
    }

    public void setTemperature(int Temperature){
        this.Temperature = Temperature;
    }

    public int getTemperature(){
        return Temperature;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
