package com.mmcs.societymaintainance.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mmcs.societymaintainance.R;
import com.mmcs.societymaintainance.model.LoginModel;
import com.mmcs.societymaintainance.model.LoginResMeta;
import com.mmcs.societymaintainance.util.Shprefrences;
import com.mmcs.societymaintainance.util.Singleton;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText edt_username, edt_password;
    Button loginBtn;
    TextView btn_register;
    Animation animShake;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    RelativeLayout relativeLayout;
    private boolean internetConnected = true;
    Spinner spnLoginType;
    public static String fcmToken;
    Shprefrences sh;
    public static String registerEnabled="false";
ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        animShake = AnimationUtils.loadAnimation(this, R.anim.shake_login);
        loginBtn = findViewById(R.id.loginBtn);
        btn_register = findViewById(R.id.btn_register);
        relativeLayout = findViewById(R.id.relativeLayout);
        spnLoginType = findViewById(R.id.spnUserType);
        sh = new Shprefrences(this);
        progress=findViewById(R.id.progress);
        if(registerEnabled.equalsIgnoreCase("true"))
            btn_register.setVisibility(View.VISIBLE);

        String typeList[] = {"Select Login Types", "Admin", "Owner", "Employee", "Renter"};//,"Super Admin"
        spnLoginType.setAdapter(new ArrayAdapter(this, R.layout.spn_textview_item, R.id.spn_txt_item, typeList));
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterNewUserActivity.class);
                startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name = edt_username.getText().toString();
                String pass = edt_password.getText().toString();
                if ((user_name.equals(""))) {
                    edt_username.startAnimation(animShake);
                    Toast.makeText(LoginActivity.this, getString(R.string.email), Toast.LENGTH_SHORT).show();
                    return;
                } else if (pass.equals("")) {
                    edt_password.startAnimation(animShake);
                    Toast.makeText(LoginActivity.this, getString(R.string.password), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (spnLoginType.getSelectedItemPosition() == 0) {
                    Toast.makeText(LoginActivity.this, R.string.select_logintype, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    progress.setVisibility(View.VISIBLE);
                    getLogin(user_name, pass, spnLoginType.getSelectedItemPosition() + "");

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    /**
     * Method to register runtime broadcast receiver to show snackbar alert for internet connection..
     */
    private void registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, internetFilter);
    }

    /**
     * Runtime Broadcast receiver inner class to capture internet connectivity events
     */
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = getConnectivityStatusString(context);
            setSnackbarMessage(status, false);
        }
    };

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

    private void setSnackbarMessage(String status, boolean showBar) {
        String internetStatus = "";
        if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {
            internetStatus = "Internet Connected";
        } else {
            internetStatus = "Lost Internet Connection";
        }
        snackbar = Snackbar
                .make(relativeLayout, internetStatus, Snackbar.LENGTH_LONG)
                .setAction("X", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
        snackbar.setActionTextColor(Color.WHITE);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        if (internetStatus.equalsIgnoreCase("Lost Internet Connection")) {
            if (internetConnected) {
                snackbar.show();
                internetConnected = false;
            }
        } else {
            if (!internetConnected) {
                internetConnected = true;
                snackbar.show();
            }
        }
    }

    public void showWelcomeTitle() {
        Context context = getApplicationContext();
        LayoutInflater inflater = getLayoutInflater();
        View wlcm_Toast = inflater.inflate(R.layout.welcome_toast, null);
        Toast wlcmtoast = new Toast(context);
        wlcmtoast.setView(wlcm_Toast);
        wlcmtoast.setGravity(Gravity.BOTTOM, 0, 0);
        wlcmtoast.setDuration(Toast.LENGTH_SHORT);
        wlcmtoast.show();
    }


    public void getLogin(String username, String password, final String loginType) {
        Singleton.getInstance().getApi().login(username, password, "", fcmToken, "", loginType).enqueue(new Callback<LoginResMeta>() {
            @Override
            public void onResponse(Call<LoginResMeta> call, Response<LoginResMeta> response) {
                if (response.body().getCode().equalsIgnoreCase("200")) {
                    sh.setString("TYPE", spnLoginType.getSelectedItem() + "");
                    sh.setBoolean("ISLOGIN", true);
                    LoginModel model = response.body().getResponse().get(0);
                    Log.e("loginModel.getId()","**************loginModel.getId()"+model.getId());
                    model.setType(loginType);
                    sh.setLoginModel(getString(R.string.login_model), model);
                    startActivity(new Intent(LoginActivity.this, DrawerActivity.class));
                    showWelcomeTitle();
                    finish();
                } else {
                    Toasty.error(LoginActivity.this, "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<LoginResMeta> call, Throwable t) {

            }
        });
    }
}
