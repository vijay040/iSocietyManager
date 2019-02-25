package com.mmcs.societymaintainance.adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mmcs.societymaintainance.R;
import com.mmcs.societymaintainance.activity.AttendanceActivity;
import com.mmcs.societymaintainance.activity.BillActivity;
import com.mmcs.societymaintainance.activity.ComplaintListActivity;
import com.mmcs.societymaintainance.activity.DriverActivity;
import com.mmcs.societymaintainance.activity.EmployeeListActivity;
import com.mmcs.societymaintainance.activity.GaurdActivity;
import com.mmcs.societymaintainance.activity.LoginActivity;
import com.mmcs.societymaintainance.activity.MaidActivity;
import com.mmcs.societymaintainance.activity.OthersActivity;
import com.mmcs.societymaintainance.activity.OwnerListActivity;
import com.mmcs.societymaintainance.activity.ProfileActivity;
import com.mmcs.societymaintainance.activity.VisitorListActivity;
import com.mmcs.societymaintainance.model.HomeItemModel;
import com.mmcs.societymaintainance.util.Shprefrences;

import java.util.ArrayList;


public class HomeRecyclerAdaptor  extends RecyclerView.Adapter<HomeRecyclerAdaptor.ViewHolder> {

    ArrayList<HomeItemModel> list;
    Context context;
    private LayoutInflater mInflater;
    ImageView imgUserProfile;
    TextView txtTitle;
    RelativeLayout layUser;
    Shprefrences sh;
    public HomeRecyclerAdaptor(Context context, ArrayList<HomeItemModel> list)
    {
        this.list=list;
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        sh=new Shprefrences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_home_inf, parent, false);
         imgUserProfile=view.findViewById(R.id.imgUserProfile);
        txtTitle=view.findViewById(R.id.txt_title);
        layUser=view.findViewById(R.id.layUser);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        txtTitle.setText(list.get(position).getTitle()+"");
       // Uri uri = Uri.parse("android.resource://com.mmcs.societymaintainance/drawable/"+list.get(position).getImage());
        imgUserProfile.setBackground(context.getResources().getDrawable(list.get(position).getImage()));
        //Picasso.get().load(uri).into(imgUserProfile);

        if(list.get(position).getTitle().equalsIgnoreCase("Logout")) {
            layUser.setVisibility(View.GONE);
        }
        layUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (list.get(position).getTitle())
                {
                    case "Profile":
                        context.startActivity(new Intent(context, ProfileActivity.class));
                        break;
                    case "Add Owner":
                        context.startActivity(new Intent(context, OwnerListActivity.class));
                        break;
                    case "Attendance":
                        context.startActivity(new Intent(context, AttendanceActivity.class));
                        break;
                    case "Complaint":
                        context.startActivity(new Intent(context, ComplaintListActivity.class));
                        break;
                    case "Visitor":
                        context.startActivity(new Intent(context, VisitorListActivity.class));
                        break;

                    case "Employee":
                        context.startActivity(new Intent(context, EmployeeListActivity.class));
                        break;

                    case "Driver":
                        context.startActivity(new Intent(context, DriverActivity.class));
                        break;
                    case "Maid":
                        context.startActivity(new Intent(context, MaidActivity.class));
                        break;
                    case "Your Bill":
                        context.startActivity(new Intent(context, BillActivity.class));
                        break;
                    case "Guard":
                        context.startActivity(new Intent(context, GaurdActivity.class));
                        break;
                    case "Others":
                        context.startActivity(new Intent(context, OthersActivity.class));
                        break;

                    case "Logout":
                        sh.clearData();
                        Toast.makeText(context, context.getString(R.string.you_have_logged_out_successfully), Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(context, LoginActivity.class);
                        context.startActivity(in);
                        break;

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);


        }
    }
}
