package com.mmcs.societymaintainance.adaptor;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mmcs.societymaintainance.R;
import com.mmcs.societymaintainance.model.ComplaintModel;

import java.util.ArrayList;

/**
 * Created by Lenovo on 24-10-2018.
 */

public class ComplaintAdapter extends BaseAdapter {
    public ArrayList<ComplaintModel> list;
    public Activity context;

    public ComplaintAdapter(Activity context, ArrayList<ComplaintModel> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(list!=null)
            return list.size();
        else return 0;
    }
    public void filter(ArrayList<ComplaintModel> newList) {
        list = new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_complaintlist_item, null);
        }
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        txtTitle.setVisibility(View.GONE);
        txtTitle.setText(context.getString(R.string.titl)+list.get(i).getTitle());
        TextView txtDate = view.findViewById(R.id.txtDate);
        TextView txtDepartment= view.findViewById(R.id.txtDepartment);
        txtDate.setText(context.getString(R.string.date)+list.get(i).getDate());
        txtDepartment.setText("Department:"+list.get(i).getDepartment());

        TextView txt_c_des= view.findViewById(R.id.txt_c_des);
        txt_c_des.setText(context.getString(R.string.desc)+list.get(i).getC_description());
        TextView txtstatus=view.findViewById(R.id.txtstatus);
        txtstatus.setText(context.getString(R.string.status)+list.get(i).getStatus());
        TextView txtUnit=view.findViewById(R.id.txtUnit);
        txtUnit.setText("Unit:"+list.get(i).getUnit());


        ImageView imz_down=view.findViewById(R.id.imz_down);
        final ImageView hide=view.findViewById(R.id.imz_down);
        RelativeLayout relativeLayout=view.findViewById(R.id.relativelayout);
        final RelativeLayout lay=view.findViewById(R.id.lay);
        if(list.get(i).isVisible()) {
            lay.setVisibility(View.VISIBLE);
            hide.setImageResource( R.drawable.ic_up);
        }
        else {
            lay.setVisibility(View.GONE);
            hide.setImageResource( R.drawable.ic_down);
        }

        lay.setVisibility(View.VISIBLE);
        hide.setVisibility(View.GONE);

        /*imz_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.get(i).isVisible= !list.get(i).isVisible;
                if(list.get(i).isVisible()) {
                    lay.setVisibility(View.VISIBLE);
                    hide.setImageResource(R.drawable.ic_up);
                }
                else {
                    lay.setVisibility(View.GONE);
                    hide.setImageResource( R.drawable.ic_down);
                }
            }
        });*/
        if(list.get(i).getStatus() != null && !list.get(i).getStatus().equals("")) {

            switch (list.get(i).getStatus()) {
                case "PENDING":
//Pending
                    String status = "<font color=#3F51B5>" +context.getString(R.string.status) + "</font>" + "<font color=#EF6C00>" + list.get(i).getStatus() + "</font>";
                    txtstatus.setText(Html.fromHtml(status));
                    break;

                case "RESOLVED":
                    String status1 = "<font color=#3F51B5>" +context.getString(R.string.status) + "</font>" + "<font color=#00C853>" + list.get(i).getStatus() + "</font>";
                    txtstatus.setText(Html.fromHtml(status1));
                    break;

            }
        }

        return view;
    }
}
