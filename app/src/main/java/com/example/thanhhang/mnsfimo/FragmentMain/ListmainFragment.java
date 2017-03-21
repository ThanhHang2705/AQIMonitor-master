package com.example.thanhhang.mnsfimo.FragmentMain;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.thanhhang.mnsfimo.Activities.Detail;
import com.example.thanhhang.mnsfimo.Adapters.ListLoveAdapter;
import com.example.thanhhang.mnsfimo.Data.DataFromLocalHost;
import com.example.thanhhang.mnsfimo.Data.Database;
import com.example.thanhhang.mnsfimo.KQNode;
import com.example.thanhhang.mnsfimo.Love;
import com.example.thanhhang.mnsfimo.MainActivity;
import com.example.thanhhang.mnsfimo.R;
import com.example.thanhhang.mnsfimo.Setting.Notification;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListmainFragment extends Fragment {
    ListView list;
    ListLoveAdapter adapter;
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
        adapter = new ListLoveAdapter(listLove,getContext());
        list.setAdapter(adapter);
        size = list.getCount();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                DataFromLocalHost dataFromLocalHost = new DataFromLocalHost();
                dataFromLocalHost.execute();
                String AllData = null;
                try {
                    AllData = dataFromLocalHost.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                final int position1 = position;
                AlertDialog.Builder adb=new AlertDialog.Builder(getContext());
                adb.setTitle("What do you want?");
                final int positionToRemove = position;
                final String finalAllData = AllData;
                adb.setNegativeButton("Detail", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(finalAllData!=""){
                            Bundle bundle = new Bundle();
                            bundle.putString("AllData", finalAllData);
                            TextView PM = (TextView) list.getChildAt(position).findViewById(R.id.txt_pm);
                            String pm = PM.getText().toString();
                            int pm_int = Integer.parseInt(pm);
                            bundle.putInt("PM", Integer.parseInt(pm));
                            TextView diadiem = (TextView) list.getChildAt(positionToRemove).findViewById(R.id.txt_diadiem);
                            String NameNode = diadiem.getText().toString();
                            bundle.putString("Address",NameNode);
                            Intent intent = new Intent(getContext(), Detail.class);
                            intent.putExtra("TapTin", bundle);
                            startActivity(intent);
                        }

                    }
                });
                adb.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

//                        listLove.remove(positionToRemove);
//                        adapter = new ListLoveAdapter(listLove,getContext());
//                        list.setAdapter(adapter);
                        TextView diadiem = (TextView) list.getChildAt(positionToRemove).findViewById(R.id.txt_diadiem);
                        String NameNode = diadiem.getText().toString();
                        ArrayList<KQNode> temp = ((MainActivity)getActivity()).getDataFromSQLite();
                        for (int i=0 ;i<temp.size();i++){
                            String nameNode = temp.get(i).getNameNode();
                            if(NameNode.equals(nameNode)){
//                                Toast.makeText(getContext(),temp.get(i).getID(),Toast.LENGTH_LONG).show();
                                deleteDataInFavourite(temp.get(i).getID());
                                break;
                            }
                        }
                        listLove.remove(positionToRemove);
                        UpdateListLove();
                        int size = listLove.size();
                        adapter = new ListLoveAdapter(listLove,getContext());
                        list.setAdapter(adapter);
                        ArrayList<Fragment> al = (ArrayList<Fragment>) getFragmentManager().getFragments();

                        Fragment removeFragment = null;
                        for (int i=0;i<al.size();i++){
                            removeFragment = al.get(i);
                        }
                        removeFragment = al.get(2);
                        FragmentManager FM = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = FM.beginTransaction();





                        fragmentTransaction.detach(removeFragment);
                        fragmentTransaction.attach(removeFragment);
                        fragmentTransaction.commit();


                    }});
                adb.setNeutralButton("Notification", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();
                        TextView diadiem = (TextView) list.getChildAt(positionToRemove).findViewById(R.id.txt_diadiem);
                        String NameNode = diadiem.getText().toString();
                        bundle.putString("NameNode",NameNode);
                        TextView pm = (TextView) list.getChildAt(positionToRemove).findViewById(R.id.txt_pm);
                        String PM25 = pm.getText().toString();
                        bundle.putString("PM25",PM25);
                        Intent intent = new Intent(getContext(), Notification.class);
                        intent.putExtra("TapTin", bundle);
                        startActivity(intent);
                    }
                });
                adb.show();


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
        adapter = new ListLoveAdapter(listLove,getContext());
        list.setAdapter(adapter);
    }

    public void deleteDataInFavourite(int ID){
        SQLiteDatabase database = Database.initDatabase(getActivity(),"List_Favourite.sqlite");
        Cursor cursor = database.rawQuery("SELECT * FROM Favourite", null);
        int size = cursor.getCount();
        database.delete("Favourite","ID = ?", new String[]{String.valueOf(ID)});
        cursor = database.rawQuery("SELECT * FROM Favourite", null);
        size = cursor.getCount();
        int i = 0;
    }
}
