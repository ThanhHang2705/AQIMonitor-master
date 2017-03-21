package com.example.thanhhang.mnsfimo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thanhhang.mnsfimo.KQNode;
import com.example.thanhhang.mnsfimo.R;

import java.util.ArrayList;

/**
 * Created by ThanhHang on 12/5/2016.
 */

public class KQuaAdapter extends BaseAdapter {
    TextView txt_id;
    TextView txt_nameNode;
    TextView  txt_address;
    TextView txt_PM;
    TextView txt_temp,txt_hum, txt_latlag;
    ArrayList<KQNode> list = new ArrayList<>();
    LayoutInflater inflater;

    public KQuaAdapter(ArrayList<KQNode> list, Context context) {
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public KQNode getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = inflater.inflate(R.layout.item_list_ketqua,null);
        }
//        txt_id = (TextView) convertView.findViewById(R.id.txt_id);
        txt_nameNode = (TextView) convertView.findViewById(R.id.txt_nameNode);
        txt_address= (TextView) convertView.findViewById(R.id.txt_address);
        txt_PM = (TextView) convertView.findViewById(R.id.txt_pm);
        txt_temp = (TextView) convertView.findViewById(R.id.txt_temp);
        txt_hum = (TextView) convertView.findViewById(R.id.txt_hum);
        txt_latlag = (TextView) convertView.findViewById(R.id.txt_latlng);
        //gán giá trị
        KQNode kq = list.get(position);
//        txt_id.setText("ID : "+ kq.getID());
        txt_nameNode.setText("Ten Node: " +kq.getNameNode());
        txt_address.setText("Dia chi:" +kq.getAddress());
        txt_PM.setText("PM 2.5 : " + kq.getPM());
        txt_temp.setText("Temp: " + kq.getTemperature());
        txt_hum.setText("Hum: "+ kq.getHumidity());
        txt_latlag.setText("LatLng: "+ kq.getLatLng());

        return convertView;
    }
}
