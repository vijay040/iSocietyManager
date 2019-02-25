package com.mmcs.societymaintainance.adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mmcs.societymaintainance.R;
import com.mmcs.societymaintainance.model.FloorModel;

import java.util.ArrayList;

/**
 * Created by Lenovo on 20-10-2018.
 */

public class FloorPopupAdapter extends BaseAdapter {
    public ArrayList<FloorModel> list;
    public Activity context;

    public FloorPopupAdapter(Activity context, ArrayList<FloorModel> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public void filter(ArrayList<FloorModel> newList)
    {
        list=new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();
    }
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.inf_purpose_popup_list, null);


        }

        TextView txtTitle = view.findViewById(R.id.txtTitle);

        txtTitle.setText(list.get(i).getFloor_no());

        return view;
    }
}
