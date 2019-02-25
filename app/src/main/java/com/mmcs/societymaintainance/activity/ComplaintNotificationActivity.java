package com.mmcs.societymaintainance.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.messaging.RemoteMessage;
import com.mmcs.societymaintainance.R;
import com.mmcs.societymaintainance.model.ComplaintModel;
import com.mmcs.societymaintainance.model.ComplaintRestMeta;
import com.mmcs.societymaintainance.model.LoginModel;
import com.mmcs.societymaintainance.util.Shprefrences;
import com.mmcs.societymaintainance.util.Singleton;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplaintNotificationActivity extends AppCompatActivity {
    TextView txtDepartment, txtTitle, txtDate, txt_c_des, txtFloor, txtUnit, txtStatus;
    Button reject, accept;
    ComplaintModel model;
    LoginModel loginModel;
    ProgressBar progress;
    Shprefrences sh;
    public static MediaPlayer r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_notification);
        txtDepartment = findViewById(R.id.txtDepartment);
        txtTitle = findViewById(R.id.txtTitle);
        txtDate = findViewById(R.id.txtDate);
        txt_c_des = findViewById(R.id.txt_c_des);
        txtFloor = findViewById(R.id.txtFloor);
        txtUnit = findViewById(R.id.txtUnit);
        txtStatus = findViewById(R.id.txtStatus);
        progress = findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        accept = findViewById(R.id.accept);
        reject = findViewById(R.id.reject);
        sh = new Shprefrences(this);
        loginModel = sh.getLoginModel(getResources().getString(R.string.login_model));

        RemoteMessage remoteMessage = (RemoteMessage) getIntent().getExtras().get("NOTIFICATION_VALUE");
        String body = remoteMessage.getNotification().getBody();
        Log.e("body", "body********" + body);
        final String strID[] = body.split("#");
        getComplain(strID[1]);
        back();
        setTitle();
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                complainAction();
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (r != null) {
            r.stop();
        }

    }

    private void getComplain(String id) {

        Singleton.getInstance().getApi().getComplainById(id).enqueue(new Callback<ComplaintRestMeta>() {
            @Override
            public void onResponse(Call<ComplaintRestMeta> call, Response<ComplaintRestMeta> response) {
                model = response.body().getResponse().get(0);
                setData();
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ComplaintRestMeta> call, Throwable t) {
                progress.setVisibility(View.GONE);

            }
        });
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

    private void setTitle() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.comp_detail));
    }

    private void setData() {
        txtDepartment.setText(getString(R.string.dept) + model.getDepartment());
        txtTitle.setText(getString(R.string.titl) + model.getTitle());
        txtDate.setText(getString(R.string.date) + model.getDate());
        txt_c_des.setText(getString(R.string.desc) + model.getC_description());
        txtFloor.setText(getString(R.string.floor) + model.getFloor_no());
        txtUnit.setText(getString(R.string.unit_no) + model.getUnit_no());
        txtStatus.setText(getString(R.string.status) + model.getStatus());

        SpannableStringBuilder sb = new SpannableStringBuilder(txtDepartment.getText());
        ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));

        sb.setSpan(fcs, 0, 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txtDepartment.setText(sb);

        sb = new SpannableStringBuilder(txtTitle.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txtTitle.setText(sb);

        sb = new SpannableStringBuilder(txtFloor.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txtFloor.setText(sb);

        sb = new SpannableStringBuilder(txtUnit.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txtUnit.setText(sb);

        sb = new SpannableStringBuilder(txtDate.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txtDate.setText(sb);
        sb = new SpannableStringBuilder(txtStatus.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txtStatus.setText(sb);

    }

    private void complainAction() {
        Singleton.getInstance().getApi().complainAction(model.getComplain_id(), loginModel.getId()).enqueue(new Callback<ComplaintRestMeta>() {
            @Override
            public void onResponse(Call<ComplaintRestMeta> call, Response<ComplaintRestMeta> response) {
                progress.setVisibility(View.GONE);
                ComplaintModel m=response.body().getResponse().get(0);
                if(m.getCode().equalsIgnoreCase("200"))
                {
                    Toasty.success(ComplaintNotificationActivity.this, ""+m.getMessage()).show();
                    finish();
                }
                else
                    Toasty.error(ComplaintNotificationActivity.this, ""+m.getMessage()).show();
            }

            @Override
            public void onFailure(Call<ComplaintRestMeta> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Toasty.error(ComplaintNotificationActivity.this, "Please try again!").show();
            }
        });
    }
}
