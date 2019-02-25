package com.mmcs.societymaintainance.activity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mmcs.societymaintainance.R;
import com.mmcs.societymaintainance.model.AttandenceModel;
import com.mmcs.societymaintainance.model.LoginModel;
import com.mmcs.societymaintainance.model.ResAttandance;
import com.mmcs.societymaintainance.util.AppLocationService;
import com.mmcs.societymaintainance.util.MyLocation;
import com.mmcs.societymaintainance.util.Shprefrences;
import com.mmcs.societymaintainance.util.Singleton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceActivity extends AppCompatActivity {
    ImageView signin;
    TextView textViewsignin;
    LocationManager locationManager;
    public static String currentLocation = "";
    private String status = "signin";
    boolean isLogin = false;
    Shprefrences sh;
    ProgressBar progress;
    TextView txtLocation, texDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        sh = new Shprefrences(this);
        setContentView(R.layout.activity_attendance);
        progress = findViewById(R.id.progressbar);
        signin = findViewById(R.id.imageView);
        texDate = findViewById(R.id.texDate);
        textViewsignin = findViewById(R.id.textview_signin);
        txtLocation = findViewById(R.id.current_location);
        txtLocation.setText(currentLocation);
        getLocation();
        DateFormat df = new SimpleDateFormat(getString(R.string.date_formate));
        final String createddate = df.format(Calendar.getInstance().getTime());
        texDate.setText(createddate);
        getAttandanceStatus();
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                postAttandance();
            }
        });
        back();
        setTitle();
    }

    AppLocationService appLocationService;
    Location nwLocation;

    public void getLocation() {
        appLocationService = new AppLocationService(AttendanceActivity.this);
        nwLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);
        if (nwLocation != null) {
            MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                @Override
                public void gotLocation(Location location) {
                    //Got the location!
                    progress.setVisibility(View.VISIBLE);
                    try {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            currentLocation = GetAddress(latitude, longitude);
                            txtLocation.setText(currentLocation);
                            // text_location.setText(location);
                            Log.e("Loaction********", currentLocation);
                            progress.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.getMessage();
                        progress.setVisibility(View.GONE);
                    }
                }
            };
            MyLocation myLocation = new MyLocation();
            myLocation.getLocation(this, locationResult);
        } else {
            // showSettingsAlert("NETWORK");
        }
    }

    public String GetAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String city = "", state = "", address = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Log.d("Addrss", addresses + "");
            // latlong = new LatLng(latitude, longitude);
            // address = addresses.get(0).getAddressLine(0) + " " + addresses.get(0).getAddressLine(1) + " " + addresses.get(0).getAddressLine(2);
            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getAddressLine(1);
            state = addresses.get(0).getAdminArea();
            String zip = addresses.get(0).getPostalCode();
            String country = addresses.get(0).getCountryName();
        } catch (Exception e) {

        }
        return address;
    }

    private void setTitle() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.attendance));
    }

    private void back() {
        RelativeLayout drawerIcon = (RelativeLayout) findViewById(R.id.drawerIcon);
        drawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void postAttandance() {
        LoginModel model = sh.getLoginModel(getString(R.string.login_model));
        DateFormat df = new SimpleDateFormat(getString(R.string.date_formate));
        final String createddate = df.format(Calendar.getInstance().getTime());

        if (currentLocation.equalsIgnoreCase("")) {
            Toast.makeText(AttendanceActivity.this, "Please wait while fetching location!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (status.equalsIgnoreCase("signin")) {
            status = "signout";
            signin.setBackground(ContextCompat.getDrawable(AttendanceActivity.this, R.drawable.ic_signin));
            textViewsignin.setText(getString(R.string.sign_in));
        } else {
            status = "signin";
            signin.setBackground(ContextCompat.getDrawable(AttendanceActivity.this, R.drawable.ic_signout));
            textViewsignin.setText(getString(R.string.logout));
        }
        Singleton.getInstance().getApi().postAttendance(model.getId(), currentLocation, createddate, status, "",model.getType()).enqueue(new Callback<ResAttandance>() {
            @Override
            public void onResponse(Call<ResAttandance> call, Response<ResAttandance> response) {
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ResAttandance> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }

    private void getAttandanceStatus() {
        progress.setVisibility(View.VISIBLE);
        LoginModel model = sh.getLoginModel(getString(R.string.login_model));
        Singleton.getInstance().getApi().getAttandanceStatus(model.getId(),model.getType()).enqueue(new Callback<ResAttandance>() {
            @Override
            public void onResponse(Call<ResAttandance> call, Response<ResAttandance> response) {

                ArrayList<AttandenceModel> model = response.body().getResponse();
                if (model.size() > 0)
                    status = model.get(0).getStatus();
                if (status.equalsIgnoreCase("signin")) {
                    signin.setBackground(ContextCompat.getDrawable(AttendanceActivity.this, R.drawable.ic_signout));
                    textViewsignin.setText(getString(R.string.signout));
                } else {
                    signin.setBackground(ContextCompat.getDrawable(AttendanceActivity.this, R.drawable.ic_signin));
                    textViewsignin.setText(getString(R.string.sign_in));
                }
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ResAttandance> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }
}
