package com.example.thanhhang.mnsfimo.Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thanhhang.mnsfimo.KQNode;
import com.example.thanhhang.mnsfimo.R;

import java.util.ArrayList;

/**
 * Created by HP on 3/23/2017.
 */

public class ListNodeAdapter extends BaseAdapter{
    TextView textView;
    ImageView imageView;
    ArrayList<KQNode> ListNode;
    ArrayList<KQNode> ListLove;
    LayoutInflater inflater;
    public static ArrayList<String> temporarylist;

    public ListNodeAdapter(ArrayList<KQNode> listNode,ArrayList<KQNode>ListLove, Context context) {
        this.ListNode = listNode;
        this.ListLove = ListLove;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return ListNode.size();
    }

    @Override
    public Object getItem(int position) {
        return ListNode.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ListNode.get(position).getID();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if(view==null){
            view = inflater.inflate(R.layout.item_list_all_node,null);
        }
        textView=(TextView)view.findViewById(R.id.txt_Node);
        imageView = (ImageView)view.findViewById(R.id.favourite_status);
        final KQNode kqNode = ListNode.get(position);
        textView.setText(kqNode.getNameNode());

//        for (int i =0;i<ListLove.size();i++){
//            KQNode kqNode_favourite = ListLove.get(i);
//            if(kqNode.getNameNode().equals(kqNode_favourite.getNameNode())){
//                imageView.setImageResource(R.drawable.favourite);
//            }
//        }
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String NameNode = kqNode.getNameNode();
//                boolean check = false;
//                for(int i=0;i<ListLove.size();i++){
//                    if(NameNode.equals(ListLove.get(i).getNameNode())){
//                        imageView.setImageResource(R.drawable.not_favourite);
//                        ListLove.remove(i);
//                        check = true;
//                        break;
//                    }
//                }
//                if (check=false){
//                    imageView.setImageResource(R.drawable.favourite);
//                    ListLove.add(kqNode);
//                }
//
//            }
//        });
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }


//    @Override
//    public Filter getFilter() {
//        Filter filter = new Filter() {
//
//            @SuppressWarnings("unchecked")
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                temporarylist=(ArrayList<String>)results.values;
//                notifyDataSetChanged();
//            }
//
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                FilterResults results = new FilterResults();
//                ArrayList<String> FilteredList= new ArrayList<String>();
//                if (constraint == null || constraint.length() == 0) {
//                    // No filter implemented we return all the list
//                    results.values = ListNode;
//                    results.count = ListNode.size();
//                }
//                else {
//                    for (int i = 0; i < ListNode.size(); i++) {
//                        String data = ListNode.get(i).getNameNode();
//                        if (data.toLowerCase().contains(constraint.toString()))  {
//                            FilteredList.add(data);
//                        }
//                    }
//                    results.values = FilteredList;
//                    results.count = FilteredList.size();
//                }
//                return results;
//            }
//        };
//        return filter;
//    }
}
