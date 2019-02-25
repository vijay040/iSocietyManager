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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.google.firebase.messaging.RemoteMessage;
import com.mmcs.societymaintainance.R;
import com.mmcs.societymaintainance.model.UnitRestMeta;
import com.mmcs.societymaintainance.model.VisitorModel;
import com.mmcs.societymaintainance.model.VisitorRestMeta;
import com.mmcs.societymaintainance.util.Singleton;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitorNotificationActivity extends AppCompatActivity {
    TextView txtName, txt_mobile, txt_address, txtFloor, txtUnit, txtIntime;
    ImageView image_visitor;
    Button reject,accept;
    ProgressBar progress;
    VisitorModel visitorModels = new VisitorModel();
    public static MediaPlayer r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_notification);
        txtName = findViewById(R.id.txtName);
        txt_mobile = findViewById(R.id.txt_mobile);
        txt_address = findViewById(R.id.txt_address);
        txtIntime = findViewById(R.id.txtIntime);
        txtFloor = findViewById(R.id.txtFloor);
        txtUnit = findViewById(R.id.txtUnit);
        progress=findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        image_visitor = findViewById(R.id.image_visitor);
        reject=findViewById(R.id.reject);
        accept=findViewById(R.id.accept);
        RemoteMessage remoteMessage = (RemoteMessage) getIntent().getExtras().get("NOTIFICATION_VALUE");
        String body = remoteMessage.getNotification().getBody();
        Log.e("body", "body********" + body);
        final String strID[] = body.split("#");
        getVisitor(strID[1]);
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                updateStatus(strID[1],"REJECTED");
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                updateStatus(strID[1],"APPROVED");
            }
        });


        if (r != null) {
            r.stop();
        }
        Log.e("strID size" + strID.length, "***********************************id*****" + strID[1]);
    }



    public  void getVisitor(String id)
    {
        Singleton.getInstance().getApi().getVisitorById(id).enqueue(new Callback<VisitorRestMeta>() {
            @Override
            public void onResponse(Call<VisitorRestMeta> call, Response<VisitorRestMeta> response) {
                if(response.body().getResponse().size()>0) {
                    visitorModels = response.body().getResponse().get(0);
                    setData();
                }
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<VisitorRestMeta> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });
    }

    private void setData()
    {
        txtName.setText(getString(R.string.name) + visitorModels.getName());
        txt_mobile.setText(getString(R.string.mobile_no) + visitorModels.getMobile());
        txt_address.setText(getString(R.string.address) + visitorModels.getAddress());
        txtFloor.setText(getString(R.string.floor) + visitorModels.getFloor_no());
        txtUnit.setText(getString(R.string.unit_no) + visitorModels.getUnit_no());
        txtIntime.setText(getString(R.string.time_in)+visitorModels.getIntime());
        Glide.with(this).load(visitorModels.getImage()).placeholder(R.drawable.no_image).into(image_visitor);
        image_visitor.setOnTouchListener(new ImageMatrixTouchHandler(VisitorNotificationActivity.this));
        SpannableStringBuilder sb = new SpannableStringBuilder(txtName.getText());
        // Picasso.get().load(expensemodel.getImage()).placeholder(R.drawable.ic_bill).resize(100,100).into(image_uploaded);
        // Span to set text color to some RGB value
        ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        // Span to make text bold
        //    final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        // Set the text color for first 4 characters
        sb.setSpan(fcs, 0, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txtName.setText(sb);

        sb = new SpannableStringBuilder(txt_mobile.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 10, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txt_mobile.setText(sb);

        sb = new SpannableStringBuilder(txt_address.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txt_address.setText(sb);

        sb = new SpannableStringBuilder(txtFloor.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txtFloor.setText(sb);

        sb = new SpannableStringBuilder(txtUnit.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txtUnit.setText(sb);

        sb = new SpannableStringBuilder(txtIntime.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txtIntime.setText(sb);

    }

    private void updateStatus(String id, String status)
    {
        Singleton.getInstance().getApi().updateVisitorStatus(id,status).enqueue(new Callback<UnitRestMeta>() {
            @Override
            public void onResponse(Call<UnitRestMeta> call, Response<UnitRestMeta> response) {
                progress.setVisibility(View.GONE);
                Toasty.success(VisitorNotificationActivity.this, "Data Successfully Submitted",
                        Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<UnitRestMeta> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Toasty.error(VisitorNotificationActivity.this,"Sorry Try Again", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
