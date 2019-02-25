package com.mmcs.societymaintainance.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mmcs.societymaintainance.R;

import es.dmoral.toasty.Toasty;

public class RegistrationActivity extends AppCompatActivity {
    Button btn_register;

    EditText edt_name,edt_contact_no,edt_email_address,edt_password,edt_cnfrm_password,name_edit_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        edt_name=findViewById(R.id.edt_name);
        edt_contact_no=findViewById(R.id.edt_contact_no);
        edt_email_address=findViewById(R.id.edt_email_address);
        edt_password=findViewById(R.id.edt_password);
        edt_cnfrm_password=findViewById(R.id.edt_cnfrm_password);
        name_edit_text=findViewById(R.id.name_edit_text);
        btn_register=findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=edt_name.getText().toString();
                String mob=edt_contact_no.getText().toString();
                String email=edt_email_address.getText().toString();
                String pass=edt_password.getText().toString();
                String cnfrm_pass=edt_cnfrm_password.getText().toString();
                if (name.equals("")){
                    Toasty.error(RegistrationActivity.this,getString(R.string.enter_name),Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(mob.equals("")){
                    Toasty.error(RegistrationActivity.this,getString(R.string.enter_contact),Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(email.trim().isEmpty()||!email.matches("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+")) {

                    Toasty.error(RegistrationActivity.this,getString(R.string.enter_email),Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(pass.equals("")){
                    Toasty.error(RegistrationActivity.this,getString(R.string.password),Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(cnfrm_pass.equals("")){
                    Toasty.error(RegistrationActivity.this,getString(R.string.enter_cnfrm_pass),Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!pass.equals(cnfrm_pass)){
                    Toasty.error(RegistrationActivity.this,getString(R.string.password_not_match),Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Toasty.success(RegistrationActivity.this,getString(R.string.successfully_registered),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
