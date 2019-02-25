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
import com.mmcs.societymaintainance.model.OwnerModel;

import java.util.ArrayList;

/**
 * Created by Lenovo on 23-10-2018.
 */

public class OwnerAdapter  extends BaseAdapter {
    public ArrayList<OwnerModel> list;
    public Activity context;

    public OwnerAdapter(Activity context, ArrayList<OwnerModel> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(list!=null)
            return list.size();
        else return 0;
    }
    public void filter(ArrayList<OwnerModel> newList) {
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
            view = inflater.inflate(R.layout.activity_ownerlist_item, null);
        }
        TextView txtName = view.findViewById(R.id.txtName);
        txtName.setText(context.getString(R.string.name)+list.get(i).getName());
        TextView txtMobile=view.findViewById(R.id.txtMobile);
        txtMobile.setText(context.getString(R.string.mobile_no)+list.get(i).getContact());
        TextView txtFloor=view.findViewById(R.id.txtFloor);
        txtFloor.setText(context.getString(R.string.floor)+list.get(i).getFloor_no());

        TextView txtUnit_no=view.findViewById(R.id.txtUnit_no);
        txtUnit_no.setText(context.getString(R.string.unit_no)+list.get(i).getUnit_no());

        TextView txtEmail=view.findViewById(R.id.txtEmail);
        txtEmail.setText(context.getString(R.string.email_add)+list.get(i).getEmail());

   final ImageView img = view.findViewById(R.id.img);
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

        imz_down.setOnClickListener(new View.OnClickListener() {
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
        });
        /*Glide.with(context).load(list.get(i).getImage()).placeholder(R.drawable.no_image).transform(new CircleTransform(context))
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