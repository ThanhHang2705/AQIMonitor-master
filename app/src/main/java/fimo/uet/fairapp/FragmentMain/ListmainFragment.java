package fimo.uet.fairapp.FragmentMain;


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

import java.util.ArrayList;

import fimo.uet.fairapp.Activities.LoaderUI;
import fimo.uet.fairapp.Adapters.KQuaAdapter;
import fimo.uet.fairapp.Data.Database;
import fimo.uet.fairapp.DatabaseManager.GetDataOfFairBox;
import fimo.uet.fairapp.DatabaseManager.User_DB;
import fimo.uet.fairapp.KQNode;
import fimo.uet.fairapp.Love;
import fimo.uet.fairapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListmainFragment extends Fragment {
    ListView list;
    KQuaAdapter adapter;
    private ArrayList<KQNode> listLove;
    private ArrayList<String> PID;
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
        UpdatePID();
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
                bundle.putInt("CurrentFragment",1);
                TextView diadiem = (TextView) list.getChildAt(position).findViewById(R.id.txt_nameNode);
                String NameNode = diadiem.getText().toString();
                bundle.putString("Address",NameNode);
//                ArrayList<KQNode> temp = new ALL_DATA(getContext()).ReadAllData();
                bundle.putString("ID",listLove.get(position).getID());
                Intent intent = new Intent(getContext(), LoaderUI.class);
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
//
//        listLove = new FavouriteNodeData(getContext()).GetFavouriteList();
        User_DB user_db = new User_DB(getContext());
        listLove = new ArrayList<KQNode>();
        GetDataOfFairBox getDataOfFairBox = new GetDataOfFairBox(getContext());
        listLove = getDataOfFairBox.getFavouriteData();
//        listLove = user_db.GetFavouriteList();

    }

    public void UpdatePID(){
        for (int i=0;i<listLove.size();i++){
            PID = new ArrayList<>();
            PID.add(listLove.get(i).getID());
        }
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
