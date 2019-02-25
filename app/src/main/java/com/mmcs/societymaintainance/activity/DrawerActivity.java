package com.mmcs.societymaintainance.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mmcs.societymaintainance.R;
import com.mmcs.societymaintainance.adaptor.SideBarAdaptor;
import com.mmcs.societymaintainance.fragment.FragmentHome;
import com.mmcs.societymaintainance.model.HomeItemModel;
import com.mmcs.societymaintainance.model.LoginModel;
import com.mmcs.societymaintainance.model.UnitRestMeta;
import com.mmcs.societymaintainance.util.Shprefrences;
import com.mmcs.societymaintainance.util.Singleton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrawerActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    RelativeLayout drawerIcon;
    ListView listView;
    TextView txtName, txtEmail, txtFlateNo;
    public static FragmentManager fragmentManager;
    Shprefrences sh;
    public static ArrayList<HomeItemModel> list;
    LoginModel loginModel;
    ImageView imgProfile;
    ImageView imgDrawer;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        imgProfile = findViewById(R.id.imgProfile);
        txtFlateNo = findViewById(R.id.txtFlateNo);
        listView = findViewById(R.id.listItem);
        sh = new Shprefrences(this);
        loginModel = sh.getLoginModel(getString(R.string.login_model));
        updateToken();
        txtName.setText(loginModel.getName());
        txtEmail.setText(loginModel.getEmail());
        txtFlateNo.setText(loginModel.getUnit());
        Glide.with(this).load(loginModel.getImage()).asBitmap().centerCrop().dontAnimate().placeholder(R.drawable.ic_userlogin).error(R.drawable.ic_userlogin).into(new BitmapImageViewTarget(imgProfile) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imgProfile.setImageDrawable(circularBitmapDrawable);
            }
        });
        String type = sh.getString("TYPE", "");

        HomeItemModel item = new HomeItemModel();

        if (type.equalsIgnoreCase("Owner")) {
            list = new ArrayList<>();
            item.setImage(R.drawable.ic_user);
            item.setTitle("Profile");
            list.add(item);

            item = new HomeItemModel();
            item.setTitle("Complaint");
            item.setImage(R.drawable.ic_complain);
            list.add(item);

            item = new HomeItemModel();
            item.setTitle("Visitor");
            item.setImage(R.drawable.ic_visitor);
            list.add(item);


            item = new HomeItemModel();
            item.setTitle("Driver");
            item.setImage(R.drawable.ic_driver);
            list.add(item);

            item = new HomeItemModel();
            item.setTitle("Maid");
            item.setImage(R.drawable.ic_maid);
            list.add(item);

            /*item = new HomeItemModel();
            item.setTitle("Your Bill");
            item.setImage(R.drawable.ic_purse);
            list.add(item);*/

            item = new HomeItemModel();
            item.setTitle("Logout");
            item.setImage(R.drawable.logout);
            list.add(item);

        } else if (type.equalsIgnoreCase("Employee")) {
            list = new ArrayList<>();
            item.setImage(R.drawable.ic_user);
            item.setTitle("Profile");
            list.add(item);

            item = new HomeItemModel();
            item.setTitle("Attendance");
            item.setImage(R.drawable.ic_attendance);
            list.add(item);

            item = new HomeItemModel();
            item.setTitle("Complaint");
            item.setImage(R.drawable.ic_complain);
            list.add(item);

            item = new HomeItemModel();
            item.setTitle("Logout");
            item.setImage(R.drawable.logout);
            list.add(item);
        } else if (type.equalsIgnoreCase("Admin")) {
            list = new ArrayList<>();
            item.setImage(R.drawable.ic_user);
            item.setTitle("Profile");
            list.add(item);

            item = new HomeItemModel();
            item.setTitle("Add Owner");
            item.setImage(R.drawable.ic_recidency);
            list.add(item);

            item = new HomeItemModel();
            item.setTitle("Visitor");
            item.setImage(R.drawable.ic_visitor);
            list.add(item);

            item = new HomeItemModel();
            item.setTitle("Employee");
            item.setImage(R.drawable.ic_add_member);
            list.add(item);

            item = new HomeItemModel();
            item.setTitle("Complaint");
            item.setImage(R.drawable.ic_complain);
            list.add(item);

            item = new HomeItemModel();
            item.setTitle("Guard");
            item.setImage(R.drawable.ic_guard);
            list.add(item);

            item = new HomeItemModel();
            item.setTitle("Driver");
            item.setImage(R.drawable.ic_driver);
            list.add(item);

            item = new HomeItemModel();
            item.setTitle("Maid");
            item.setImage(R.drawable.ic_maid);
            list.add(item);

            item = new HomeItemModel();
            item.setTitle("Others");
            item.setImage(R.drawable.ic_team);
            list.add(item);

            item = new HomeItemModel();
            item.setTitle("Logout");
            item.setImage(R.drawable.logout);
            list.add(item);

        } else if (type.equalsIgnoreCase("Renter")) {
            list = new ArrayList<>();
            item.setImage(R.drawable.ic_user);
            item.setTitle("Profile");
            list.add(item);

            item = new HomeItemModel();
            item.setTitle("Complaint");
            item.setImage(R.drawable.ic_complain);
            list.add(item);

            item = new HomeItemModel();
            item.setTitle("Visitor");
            item.setImage(R.drawable.ic_visitor);
            list.add(item);

            item = new HomeItemModel();
            item.setTitle("Driver");
            item.setImage(R.drawable.ic_driver);
            list.add(item);

            item = new HomeItemModel();
            item.setTitle("Maid");
            item.setImage(R.drawable.ic_maid);
            list.add(item);

            /*item = new HomeItemModel();
            item.setTitle("Your Bill");
            item.setImage(R.drawable.ic_purse);
            list.add(item);*/

            item = new HomeItemModel();
            item.setTitle("Logout");
            item.setImage(R.drawable.logout);
            list.add(item);
        }

        SideBarAdaptor adaptor = new SideBarAdaptor(this, list);
        listView.setAdapter(adaptor);

        fragmentManager = getSupportFragmentManager();
        pushFragment(new FragmentHome());
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerIcon = (RelativeLayout) findViewById(R.id.drawerIcon);
        imgDrawer = findViewById(R.id.imgDrawer);
        imgDrawer.setBackground(ContextCompat.getDrawable(DrawerActivity.this, R.drawable.ic_menu));
        setTitle();

        drawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    imgDrawer.setBackground(ContextCompat.getDrawable(DrawerActivity.this, R.drawable.ic_menu));
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                    imgDrawer.setBackground(ContextCompat.getDrawable(DrawerActivity.this, R.drawable.ic_back));
                }
            }
        });

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setTitle() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.home_page));
    }

    public static void pushFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.commit();
        } else {
            Log.e("DrawerActivity", "Error in creating fragment");
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
            imgDrawer.setBackground(ContextCompat.getDrawable(DrawerActivity.this, R.drawable.ic_menu));
        }
    }

    private void updateToken() {
        Singleton.getInstance().getApi().updateToken(loginModel.getId(), loginModel.getType(), LoginActivity.fcmToken).enqueue(new Callback<UnitRestMeta>() {
            @Override
            public void onResponse(Call<UnitRestMeta> call, Response<UnitRestMeta> response) {

            }

            @Override
            public void onFailure(Call<UnitRestMeta> call, Throwable t) {

            }
        });
    }


    public void logOut() {
        Singleton.getInstance().getApi().logOut(loginModel.getId(), loginModel.getType()).enqueue(new Callback<UnitRestMeta>() {
            @Override
            public void onResponse(Call<UnitRestMeta> call, Response<UnitRestMeta> response) {
                sh.clearData();
                Toast.makeText(DrawerActivity.this, getString(R.string.you_have_logged_out_successfully), Toast.LENGTH_SHORT).show();
                Intent in = new Intent(DrawerActivity.this, LoginActivity.class);
                DrawerActivity.this.startActivity(in);
                finish();
            }

            @Override
            public void onFailure(Call<UnitRestMeta> call, Throwable t) {

            }
        });
    }
}