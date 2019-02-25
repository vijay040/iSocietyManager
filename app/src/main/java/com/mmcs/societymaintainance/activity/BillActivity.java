package com.mmcs.societymaintainance.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mmcs.societymaintainance.R;

public class BillActivity extends AppCompatActivity {
EditText edt_bill_type,edt_bill_date,edt_bill_amount,edt_bank_name,edt_bill_description;
 Button btnOk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        edt_bill_type=findViewById(R.id.edt_bill_type);
        edt_bill_date=findViewById(R.id.edt_bill_date);
        edt_bill_amount=findViewById(R.id.edt_bill_amount);
        edt_bank_name=findViewById(R.id.edt_bank_name);
        edt_bill_description=findViewById(R.id.edt_bill_description);
        btnOk=findViewById(R.id.btnOk);
        setTitle();
        back();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void setTitle() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.bill_system));
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
