package com.example.thanhhang.mnsfimo.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thanhhang.mnsfimo.KQNode;
import com.example.thanhhang.mnsfimo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ThanhHang on 12/5/2016.
 */

public class KQuaAdapter extends BaseAdapter implements Filterable{
    public ArrayList<KQNode> temporarylist = new ArrayList<>();
    TextView txt_id;
    TextView txt_nameNode;
    TextView  txt_address;
    TextView txt_PM;
    TextView txt_temp,txt_hum, txt_latlag;
    ImageView imageView;
    ArrayList<KQNode> list = new ArrayList<>();
    LayoutInflater inflater;

    public KQuaAdapter(ArrayList<KQNode> list, Context context){
        this.list = list;
        this.temporarylist = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return temporarylist.size();
    }

    @Override
    public KQNode getItem(int position) {
        return temporarylist.get(position);
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
        imageView = (ImageView)convertView.findViewById(R.id.img_id);
        //gán giá trị
        int Position =position;
        KQNode kq = temporarylist.get(position);
//        KQNode kqNode = temporarylist.get(position);
//        txt_id.setText("ID : "+ kq.getID());
        int PM = Integer.parseInt(kq.getPM());
        txt_PM.setText(kq.getPM());
        if(PM>0 && PM<36){
            imageView.setImageResource(R.drawable.tot);
            txt_PM.setTextColor(Color.parseColor("#4FD0FA"));
        }else if(PM>35 && PM<76){
            imageView.setImageResource(R.drawable.binhthuong);
            txt_PM.setTextColor(Color.parseColor("#8BC441"));
        }else if(PM>75 && PM<116) {
            imageView.setImageResource(R.drawable.canhbao);
            txt_PM.setTextColor(Color.parseColor("#E0C303"));
        }else if(PM>115 && PM<151) {
            imageView.setImageResource(R.drawable.xau0);
            txt_PM.setTextColor(Color.parseColor("#FB8C04"));
        }else if(PM>150 && PM<251) {
            imageView.setImageResource(R.drawable.xau1);
            txt_PM.setTextColor(Color.parseColor("#E63930"));
        }else if(PM>250 && PM<351) {
            imageView.setImageResource(R.drawable.xau2);
            txt_PM.setTextColor(Color.parseColor("#614C84"));
        }else if(PM>350 && PM<1000) {
            imageView.setImageResource(R.drawable.xau3);
            txt_PM.setTextColor(Color.parseColor("#3C3C30"));
        }

            txt_nameNode.setText(kq.getNameNode());
        txt_address.setText(kq.getAddress());

        String Temp = String.valueOf(kq.getTemperature());
        txt_temp.setText(Temp);
        txt_hum.setText(String.valueOf(kq.getHumidity()));
        int time = 100;

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        Date Time=null;
        try {
            Time = df.parse(df.format(c.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txt_latlag.setText(Time.toString());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                temporarylist=(ArrayList<KQNode>)results.values;
                for(int i=0;i<temporarylist.size();i++){
                    int ID = temporarylist.get(i).getID();
                    String NameNode = temporarylist.get(i).getNameNode();
                }
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<KQNode> FilteredList= new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    // No filter implemented we return all the list
                    results.values = list;
                    results.count = list.size();
                }
                else {
                    for (int i = 0; i < list.size(); i++) {
                        String data = list.get(i).getNameNode();
                        if (data.toLowerCase().contains(constraint.toString()))  {
                            FilteredList.add(list.get(i));
                        }
                    }
                    results.values = FilteredList;
                    results.count = FilteredList.size();
                }
                return results;
            }
        };
        String s= filter.toString();
        return filter;
    }
}
