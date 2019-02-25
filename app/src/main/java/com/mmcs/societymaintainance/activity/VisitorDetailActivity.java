package com.mmcs.societymaintainance.activity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.mmcs.societymaintainance.R;
import com.mmcs.societymaintainance.model.LoginModel;
import com.mmcs.societymaintainance.model.UnitRestMeta;
import com.mmcs.societymaintainance.model.VisitorModel;
import com.mmcs.societymaintainance.model.VisitorRestMeta;
import com.mmcs.societymaintainance.util.Shprefrences;
import com.mmcs.societymaintainance.util.Singleton;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitorDetailActivity extends AppCompatActivity {
    VisitorModel visitorModel;
    TextView txtName, txt_mobile, txt_address, txtFloor, txtUnit, txtIntime, txt_visitorid, txtDate;
    ImageView image_visitor, imgStatus;
    Button btn_close;
    EditText edt_time_out;
    static final int TIME_DIALOG_ID = 1;
    int cur = 0;
    int H, M;
    Calendar calendar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Shprefrences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_detail);
        sh=new Shprefrences(this);
        visitorModel = (VisitorModel) getIntent().getSerializableExtra(getString(R.string.visitor_model));
        txtName = findViewById(R.id.txtName);
        txt_mobile = findViewById(R.id.txt_mobile);
        txt_address = findViewById(R.id.txt_address);
        txtIntime = findViewById(R.id.txtIntime);
        txtFloor = findViewById(R.id.txtFloor);
        txtUnit = findViewById(R.id.txtUnit);
        image_visitor = findViewById(R.id.image_visitor);
        btn_close = findViewById(R.id.btn_close);
        edt_time_out = findViewById(R.id.edt_time_out);
        imgStatus = findViewById(R.id.imgStatus);
        txt_visitorid = findViewById(R.id.txt_visitorid);
        txtDate = findViewById(R.id.txtDate);
        txtDate.setText("Visit Date:" + visitorModel.getVisit_date());
        txt_visitorid.setText("Visitor ID:" + visitorModel.getVisitor_id());
        mSwipeRefreshLayout = findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getVisitor(visitorModel.getVid());
                setData();
            }
        });
        setData();
    }

    private void setTitle() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.visitor_detail));
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

    private void updateVisitor(String outTime) {
        Singleton.getInstance().getApi().updateVisitor(visitorModel.getVid(), outTime).enqueue(new Callback<UnitRestMeta>() {
            @Override
            public void onResponse(Call<UnitRestMeta> call, Response<UnitRestMeta> response) {

                finish();
            }

            @Override
            public void onFailure(Call<UnitRestMeta> call, Throwable t) {

            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {

            case TIME_DIALOG_ID:
                System.out.println("onCreateDialog  : " + id);
                cur = TIME_DIALOG_ID;
                return new TimePickerDialog(this, onTimeSetListener, H, M, false);

        }

        return null;
    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int h, int m) {
            // timePicker.is24HourView();
            if (cur == TIME_DIALOG_ID) {
                // set selected date into textview
                if (h < 12 && h >= 0) {
                    edt_time_out.setText(String.valueOf(h) + ":" + String.valueOf(m) + " " + "AM");
                } else {
                    h -= 12;
                    if (h == 0) {
                        h = 12;
                    }
                    edt_time_out.setText(String.valueOf(h) + ":" + String.valueOf(m) + " " + "PM");
                }


            }
        }
    };


    public void getVisitor(String id) {
        Singleton.getInstance().getApi().getVisitorById(id).enqueue(new Callback<VisitorRestMeta>() {
            @Override
            public void onResponse(Call<VisitorRestMeta> call, Response<VisitorRestMeta> response) {
                visitorModel = response.body().getResponse().get(0);
                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<VisitorRestMeta> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setData() {


        calendar = Calendar.getInstance();
        H = calendar.get(Calendar.HOUR_OF_DAY);
        M = calendar.get(Calendar.MINUTE);
        back();
        setTitle();
        LoginModel loginModel = sh.getLoginModel(getString(R.string.login_model));
        if (visitorModel.getOuttime().equalsIgnoreCase("") && loginModel.getType().equalsIgnoreCase("1"))
            btn_close.setText("Update");
        else
            edt_time_out.setEnabled(false);
        if (visitorModel.getStatus() != null) {
            if (visitorModel.getStatus().equalsIgnoreCase("PENDING"))
                imgStatus.setImageResource(R.drawable.ic_stopwatch);
            else if (visitorModel.getStatus().equalsIgnoreCase("APPROVED"))
                imgStatus.setImageResource(R.drawable.ic_done);
            else if (visitorModel.getStatus().equalsIgnoreCase("REJECTED"))
                imgStatus.setImageResource(R.drawable.ic_cancel);
        }

        txtName.setText(getString(R.string.name) + visitorModel.getName());
        txt_mobile.setText(getString(R.string.mobile_no) + visitorModel.getMobile());
        txt_address.setText(getString(R.string.address) + visitorModel.getAddress());
        txtFloor.setText(getString(R.string.floor) + visitorModel.getFloor_no());
        txtUnit.setText(getString(R.string.unit_no) + visitorModel.getUnit_no());
        txtIntime.setText(getString(R.string.time_in) + visitorModel.getIntime());
        edt_time_out.setText(visitorModel.getOuttime());
        edt_time_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(TIME_DIALOG_ID);

            }
        });

        Glide.with(this).load(visitorModel.getImage()).placeholder(R.drawable.no_image).into(image_visitor);
        image_visitor.setOnTouchListener(new ImageMatrixTouchHandler(VisitorDetailActivity.this));
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


       /* sb = new SpannableStringBuilder(edt_time_out.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 9, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        edt_time_out.setText(sb);
*/
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visitorModel.getOuttime().equalsIgnoreCase("")) {
                    String time_out = edt_time_out.getText().toString();
                    if (time_out.equals("")) {
                        Toasty.error(VisitorDetailActivity.this, "Select Out Time", Toast.LENGTH_SHORT).show();
                    } else {
                        updateVisitor(edt_time_out.getText().toString());
                    }

                } else
                    finish();
            }
        });
    }
}
