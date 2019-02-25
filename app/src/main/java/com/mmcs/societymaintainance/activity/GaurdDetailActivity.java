package com.mmcs.societymaintainance.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.mmcs.societymaintainance.R;
import com.mmcs.societymaintainance.model.EmployeeModel;
import com.mmcs.societymaintainance.model.UnitRestMeta;
import com.mmcs.societymaintainance.util.Singleton;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GaurdDetailActivity extends AppCompatActivity {
    EmployeeModel employeeModel;
    TextView txtName,txt_mobile,txt_email,txt_present_address,txt_permanent_address,txt_joining_date,txtEnding,txtDesignation,txtNationalId;
    Button btn_close;
    ImageView image_employee;
    int cur = 0;
    EditText edt_ending_date;
    Calendar calendar;
    int DD, MM, YY;
    static final int DATE_DIALOG_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_detail);
        employeeModel = (EmployeeModel) getIntent().getSerializableExtra(getString(R.string.driver_model));
        txtName=findViewById(R.id.txtName);
        txt_mobile=findViewById(R.id.txt_mobile);
        txt_email=findViewById(R.id.txt_email);
        txt_present_address=findViewById(R.id.txt_present_address);
        txt_permanent_address=findViewById(R.id.txt_permanent_address);
        txt_joining_date=findViewById(R.id.txt_joining_date);
        txtDesignation=findViewById(R.id.txtDesignation);
        txtDesignation.setVisibility(View.GONE);
        txtNationalId=findViewById(R.id.txtNationalId);
        edt_ending_date=findViewById(R.id.edt_ending_date);
        txtEnding=findViewById(R.id.txtEnding);
        btn_close=findViewById(R.id.btn_close);
        image_employee=findViewById(R.id.image_employee);
        calendar = Calendar.getInstance();
        DD = calendar.get(Calendar.DAY_OF_MONTH);
        MM = calendar.get(Calendar.MONTH);
        YY = calendar.get(Calendar.YEAR);
        if (employeeModel.getEnding_date().equalsIgnoreCase(""))
            btn_close.setText("Update");
        else
            edt_ending_date.setEnabled(false);
        edt_ending_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        setTitle();
        back();
        txtName.setText(getString(R.string.name) + employeeModel.getName());
        txt_mobile.setText(getString(R.string.mobile_no) + employeeModel.getContact());
        txt_email.setText(getString(R.string.email_add) + employeeModel.getEmail());
        txt_present_address.setText(getString(R.string.present_add) + employeeModel.getPre_address());
        txt_permanent_address.setText(getString(R.string.permanent_address) + employeeModel.getPer_address());
        txt_joining_date.setText(getString(R.string.joining_date) + employeeModel.getDate());
        txtNationalId.setText(getString(R.string.national) + employeeModel.getNid());
        txtDesignation.setText(getString(R.string.designation) + employeeModel.getMember_type());
        Glide.with(this).load(employeeModel.getImage()).placeholder(R.drawable.no_image).into(image_employee);
       // Picasso.get().load(employeeModel.getImage()).transform(new CircleTransform()).placeholder(R.drawable.no_image).resize(100, 100).into(image_employee);
        image_employee.setOnTouchListener(new ImageMatrixTouchHandler(GaurdDetailActivity.this));
        edt_ending_date.setText(employeeModel.getEnding_date());
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


        sb = new SpannableStringBuilder(txt_email.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txt_email.setText(sb);

        sb = new SpannableStringBuilder(txt_present_address.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 16, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txt_present_address.setText(sb);

        sb = new SpannableStringBuilder(txt_permanent_address.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 18, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txt_permanent_address.setText(sb);


        sb = new SpannableStringBuilder(txt_joining_date.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 13, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txt_joining_date.setText(sb);

        sb = new SpannableStringBuilder(txtNationalId.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txtNationalId.setText(sb);


        sb = new SpannableStringBuilder(txtDesignation.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txtDesignation.setText(sb);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (employeeModel.getEnding_date().equalsIgnoreCase("")) {
                    String time_out=edt_ending_date.getText().toString();
                    if(time_out.equals("")){
                        Toasty.error(GaurdDetailActivity.this,"Select Ending Date", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        updateEmployee(edt_ending_date.getText().toString());
                    }

                } else
                    finish();
            }
        });
    }
    private void setTitle() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("Guard Details");
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
    private void updateEmployee(String ending_date) {
        Singleton.getInstance().getApi().updateOthers(employeeModel.getId(), ending_date,"GUARD").enqueue(new Callback<UnitRestMeta>() {
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
            case DATE_DIALOG_ID:
                System.out.println("onCreateDialog  : " + id);
                cur = DATE_DIALOG_ID;
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener, YY, MM, DD);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                return  datePickerDialog;

        }

        return null;
    }

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int d, int m, int y) {

            if (cur == DATE_DIALOG_ID) {
                // set selected date into textview
                if  ((m + 1) < 10)
                    edt_ending_date.setText(String.valueOf(d) + "-0" + String.valueOf(m + 1) + "-" + String.valueOf(y));
                else
                    edt_ending_date.setText(String.valueOf(d) + "-" + String.valueOf(m + 1) + "-" + String.valueOf(y));

            }
        }
    };

}