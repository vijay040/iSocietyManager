package com.mmcs.societymaintainance.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.mmcs.societymaintainance.R;
import com.mmcs.societymaintainance.model.OwnerModel;

public class OwnerDetailActivity extends AppCompatActivity {
OwnerModel ownerModel;
    TextView txtName,txt_mobile,txt_email,txt_present_address,txt_permanent_address,txtNationalId,txtFloor,txtUnit;
ImageView image_owner;
Button btn_close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_detail);
        ownerModel = (OwnerModel) getIntent().getSerializableExtra(getString(R.string.owner_model));
        txtName=findViewById(R.id.txtName);
        txt_mobile=findViewById(R.id.txt_mobile);
        txt_email=findViewById(R.id.txt_email);
        txt_present_address=findViewById(R.id.txt_present_address);
        txt_permanent_address=findViewById(R.id.txt_permanent_address);
        txtFloor=findViewById(R.id.txtFloor);
        txtNationalId=findViewById(R.id.txtNationalId);
        txtUnit=findViewById(R.id.txtUnit);
        btn_close=findViewById(R.id.btn_close);
        image_owner=findViewById(R.id.image_owner);
        txtName.setText(getString(R.string.name) + ownerModel.getName());
        txt_mobile.setText(getString(R.string.mobile_no) + ownerModel.getContact());
        txt_email.setText(getString(R.string.email_add) + ownerModel.getEmail());
        txt_present_address.setText(getString(R.string.present_add) + ownerModel.getPre_address());
        txt_permanent_address.setText(getString(R.string.permanent_address) + ownerModel.getPer_address());
        txtNationalId.setText(getString(R.string.national) + ownerModel.getNid());
        txtFloor.setText(getString(R.string.floor) + ownerModel.getFloor_no());
        txtUnit.setText(getString(R.string.unit_no) + ownerModel.getUnit_no());
        Glide.with(this).load(ownerModel.getImage()).placeholder(R.drawable.no_image).into(image_owner);
        image_owner.setOnTouchListener(new ImageMatrixTouchHandler(OwnerDetailActivity.this));
        setTitle();
        back();
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

        sb = new SpannableStringBuilder(txtNationalId.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 15, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txtNationalId.setText(sb);

        sb = new SpannableStringBuilder(txtFloor.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txtFloor.setText(sb);

        sb = new SpannableStringBuilder(txtUnit.getText());
        fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        sb.setSpan(fcs, 0, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        txtUnit.setText(sb);




        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void setTitle() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.owner_detail));
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
}
