package com.example.thanhhang.mnsfimo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thanhhang.mnsfimo.KQNode;
import com.example.thanhhang.mnsfimo.Love;
import com.example.thanhhang.mnsfimo.R;

import java.util.ArrayList;

/**
 * Created by ThanhHang on 12/2/2016.
 */

public class ListLoveAdapter extends BaseAdapter {
    ArrayList<KQNode> listLove;
    LayoutInflater inflater;
    TextView txt_diadiem;
    TextView txt_aqi;
    TextView txt_doAm;
    TextView txt_nhietDo;
    ImageView img_id;

    public ListLoveAdapter(ArrayList<KQNode> listLove, Context context) {
        this.listLove = listLove;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listLove.size();
    }

    @Override
    public KQNode getItem(int position) {
        return listLove.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view==null){
            view = inflater.inflate(R.layout.item_listlove,null);
        }
        txt_diadiem = (TextView) view.findViewById(R.id.txt_diadiem);
        txt_aqi = (TextView) view.findViewById(R.id.txt_pm);
        txt_doAm = (TextView) view.findViewById(R.id.txt_DoAm);
        txt_nhietDo = (TextView) view.findViewById(R.id.txt_nhietDo);

        KQNode kqNode = listLove.get(position);

        txt_diadiem.setText(kqNode.getNameNode()+"");
        txt_aqi.setText(kqNode.getPM()+"");
        txt_doAm.setText(kqNode.getHumidity()+"");
        txt_nhietDo.setText(kqNode.getTemperature()+"");
        return view;
    }
}
