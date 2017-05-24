package fimo.uet.fairapp;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ThanhHang on 12/2/2016.
 */

public class Love {
    String diadiem;
    int aqi;
    int DoAm;
    int NhietDo;
    LatLng latLng;

    public Love(String diadiem) {
        this.diadiem = diadiem;
    }

    public String getDiadiem() {
        return diadiem;
    }

    public Love(String diadiem, int aqi, int doAm, int nhietDo,LatLng latLng) {
        this.diadiem = diadiem;
        this.aqi = aqi;
        DoAm = doAm;
        NhietDo = nhietDo;
        this.latLng = latLng;
    }

    public void setDiadiem(String diadiem) {
        this.diadiem = diadiem;
    }

    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public int getDoAm() {
        return DoAm;
    }

    public void setDoAm(int doAm) {
        DoAm = doAm;
    }

    public int getNhietDo() {
        return NhietDo;
    }

    public void setNhietDo(int nhietDo) {
        NhietDo = nhietDo;
    }
}
