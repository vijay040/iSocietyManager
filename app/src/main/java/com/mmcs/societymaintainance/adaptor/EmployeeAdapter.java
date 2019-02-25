package com.mmcs.societymaintainance.adaptor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mmcs.societymaintainance.R;
import com.mmcs.societymaintainance.model.EmployeeModel;

import java.util.ArrayList;

/**
 * Created by Lenovo on 22-10-2018.
 */

public class EmployeeAdapter extends BaseAdapter {
    public ArrayList<EmployeeModel> list;
    public Activity context;

    public EmployeeAdapter(Activity context, ArrayList<EmployeeModel> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (list != null)
            return list.size();
        else return 0;
    }

    public void filter(ArrayList<EmployeeModel> newList) {
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
            view = inflater.inflate(R.layout.activity_employeelist_item, null);
        }
        TextView txtName = view.findViewById(R.id.txtName);
        txtName.setText(context.getString(R.string.name) + list.get(i).getName());
        TextView txtMobile = view.findViewById(R.id.txtMobile);
        txtMobile.setText(context.getString(R.string.mobile_no) + list.get(i).getContact());
        TextView txt_present_add = view.findViewById(R.id.txt_present_add);
        txt_present_add.setText(context.getString(R.string.address) + list.get(i).getPre_address());

        TextView txtEmail = view.findViewById(R.id.txtEmail);
        txtEmail.setText(context.getString(R.string.email_add) + list.get(i).getEmail());
        TextView txtDesignation = view.findViewById(R.id.txtDesignation);
        txtDesignation.setText(context.getString(R.string.designation) + list.get(i).getMember_type());
        if (list.get(i).getMember_type() == null)
            txtDesignation.setVisibility(View.GONE);
        TextView txt_joining_date = view.findViewById(R.id.txt_joining_date);
        txt_joining_date.setText(context.getString(R.string.joining_date) + list.get(i).getDate());


        final ImageView img = view.findViewById(R.id.img);
        ImageView imz_down = view.findViewById(R.id.imz_down);
        final ImageView hide = view.findViewById(R.id.imz_down);
        RelativeLayout relativeLayout = view.findViewById(R.id.relativelayout);
        final RelativeLayout lay = view.findViewById(R.id.lay);
        if (list.get(i).isVisible()) {
            lay.setVisibility(View.VISIBLE);
            hide.setImageResource(R.drawable.ic_up);
        } else {
            lay.setVisibility(View.GONE);
            hide.setImageResource(R.drawable.ic_down);
        }

        imz_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.get(i).isVisible = !list.get(i).isVisible;
                if (list.get(i).isVisible()) {
                    lay.setVisibility(View.VISIBLE);
                    hide.setImageResource(R.drawable.ic_up);
                } else {
                    lay.setVisibility(View.GONE);
                    hide.setImageResource(R.drawable.ic_down);
                }
            }
        });
       /* Glide.with(context).load(list.get(i).getImage()).placeholder(R.drawable.no_image).transform(new CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(img);*/
        Glide.with(context).load(list.get(i).getImage()).asBitmap().centerCrop().dontAnimate().placeholder(R.drawable.no_image).error(R.drawable.no_image).into(new BitmapImageViewTarget(img) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                img.setImageDrawable(circularBitmapDrawable);
            }
        });
        return view;
    }
}

