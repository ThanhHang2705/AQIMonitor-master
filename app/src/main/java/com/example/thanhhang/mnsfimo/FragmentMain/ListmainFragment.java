package com.example.thanhhang.mnsfimo.FragmentMain;


import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.thanhhang.mnsfimo.Activities.Detail;
import com.example.thanhhang.mnsfimo.Adapters.KQuaAdapter;
import com.example.thanhhang.mnsfimo.Data.Database;
import com.example.thanhhang.mnsfimo.KQNode;
import com.example.thanhhang.mnsfimo.Love;
import com.example.thanhhang.mnsfimo.MainActivity;
import com.example.thanhhang.mnsfimo.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListmainFragment extends Fragment {
    ListView list;
    KQuaAdapter adapter;
    private ArrayList<KQNode> listLove;
//    static ArrayList<Love> listLove= new ArrayList<>();
    static KQNode kqNode;
    static Love love;
    View view;
    public ListmainFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_listmain, null);
        list = (ListView) view.findViewById(R.id.listLove);

        LayoutInflater factory = LayoutInflater.from(getContext());
        UpdateListLove();
        int size = listLove.size();
        //list.setAdapter(null);
        size = list.getCount();
        adapter = new KQuaAdapter(listLove,getContext());
        list.setAdapter(adapter);
        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) list.getLayoutParams();

        lp.height = Resources.getSystem().getDisplayMetrics().heightPixels;
        list.setLayoutParams(lp);
        size = list.getCount();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
//
                final int position1 = position;
                Bundle bundle = new Bundle();
                TextView pm = (TextView) list.getChildAt(position).findViewById(R.id.txt_pm);
                String PM25 = pm.getText().toString();
                int pm_int = Integer.parseInt(PM25);
                bundle.putInt("CurrentFragment",1);
                bundle.putInt("PM", Integer.parseInt(PM25));
                TextView diadiem = (TextView) list.getChildAt(position).findViewById(R.id.txt_nameNode);
                String NameNode = diadiem.getText().toString();
                bundle.putString("Address",NameNode);
                ArrayList<KQNode> temp = ((MainActivity)getActivity()).getDataFromSQLite();
                for (int i=0 ;i<temp.size();i++){
                    String nameNode = temp.get(i).getNameNode();
                    if(NameNode.equals(nameNode)){
                        //                                Toast.makeText(getContext(),temp.get(i).getID(),Toast.LENGTH_LONG).show();
                        bundle.putInt("ID",temp.get(i).getID());
                        break;
                    }
                }
                Intent intent = new Intent(getContext(), Detail.class);
                intent.putExtra("TapTin", bundle);
                startActivity(intent);


            }
        });
        return view;
    }

    public ArrayList<KQNode> getListLove(){
        return listLove;
    }

    public void UpdateListLove(){

        listLove = ((MainActivity)getActivity()).getFavouriteList();

    }

    public void UpdateAdapter(){
        adapter = new KQuaAdapter(listLove,getContext());
        list.setAdapter(adapter);
    }

    public void deleteDataInFavourite(int ID){
        SQLiteDatabase database = Database.initDatabase(getActivity(),"FeatureOfInterest.sqlite");
        Cursor cursor = database.rawQuery("SELECT * FROM Favourite", null);
        int size = cursor.getCount();
        database.delete("Favourite","ID = ?", new String[]{String.valueOf(ID)});
        cursor = database.rawQuery("SELECT * FROM Favourite", null);
        size = cursor.getCount();
        int i = 0;
    }
}
