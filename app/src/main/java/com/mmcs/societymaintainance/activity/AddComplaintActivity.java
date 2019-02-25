package com.mmcs.societymaintainance.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.mmcs.societymaintainance.R;
import com.mmcs.societymaintainance.adaptor.DesignationAdapter;
import com.mmcs.societymaintainance.model.DesignationModel;
import com.mmcs.societymaintainance.model.DesignationRestMeta;
import com.mmcs.societymaintainance.model.LoginModel;
import com.mmcs.societymaintainance.model.LoginResMeta;
import com.mmcs.societymaintainance.util.Shprefrences;
import com.mmcs.societymaintainance.util.Singleton;

import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddComplaintActivity extends AppCompatActivity implements  SearchView.OnQueryTextListener{
    EditText edt_date,edt_complaint_title,edt_description,edt_department;
    Button btn_submit;
    ProgressBar progress;
    LoginModel loginModel;
    Calendar calendar;
    Shprefrences sh;
    int DD, MM, YY;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        edt_complaint_title=findViewById(R.id.edt_complaint_title);
        edt_description=findViewById(R.id.edt_description);
        btn_submit=findViewById(R.id.btn_submit);
        edt_date=findViewById(R.id.edt_date);
        progress = findViewById(R.id.progress);
        edt_department=findViewById(R.id.edt_department);
        btn_submit=findViewById(R.id.btn_submit);
        calendar = Calendar.getInstance();
        sh=new Shprefrences(this);
        DD = calendar.get(Calendar.DAY_OF_MONTH);
        MM = calendar.get(Calendar.MONTH);
        loginModel=sh.getLoginModel(getResources().getString(R.string.login_model));
        YY = calendar.get(Calendar.YEAR);
        progress.setVisibility(View.VISIBLE);
        getDepartmentList(loginModel.getId(),loginModel.getType() ,loginModel.getBranch_id());
        if ((MM + 1) < 10)
            edt_date.setText(String.valueOf(YY) + "-0" + String.valueOf(MM + 1) + "-" + String.valueOf(DD));
        else
            edt_date.setText(String.valueOf(YY) + "-" + String.valueOf(MM + 1) + "-" + String.valueOf(DD));
        edt_department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeptPopup();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=edt_complaint_title.getText().toString();
                String description=edt_description.getText().toString();
                String date=edt_date.getText().toString();
                String dept=edt_department.getText().toString();
               /* if(title.equals("")){
                    Toasty.error(AddComplaintActivity.this,"Enter Complaint Title",Toast.LENGTH_SHORT).show();
                    return;
                }
                else*/ if(description.equals("")){
                    Toasty.error(AddComplaintActivity.this,"Enter Complaint Description",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(dept.equals("")){
                    Toasty.error(AddComplaintActivity.this,"Select Department",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    progress.setVisibility(View.VISIBLE);
                  postComplaint(loginModel.getId(),loginModel.getType(),loginModel.getBranch_id(),title,description,date,date.split("-")[1],date.split("-")[0]);
                }

            }
        });
        setTitle();
        back();
    }
      private void setTitle() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.complaint_title));
    }
    private void back() {
        RelativeLayout drawerIcon = (RelativeLayout) findViewById(R.id.drawerIcon);
        drawerIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }
    private void postComplaint(String user_id, String type , String branchId, String title, String des, String date, String month, String year){
        Singleton.getInstance().getApi().postComplaint(user_id,type ,""+branchId,DeptId,title,des,date,month,year,loginModel.getFloor_no(),loginModel.getUnit_no()).enqueue(new Callback<LoginResMeta>() {
            @Override
            public void onResponse(Call<LoginResMeta> call, Response<LoginResMeta> response) {
                progress.setVisibility(View.GONE);
                Toasty.success(AddComplaintActivity.this, "Data Successfully Submitted",
                        Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<LoginResMeta> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Toasty.error(AddComplaintActivity.this,"Sorry Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }
    AlertDialog alertDialog;
    ArrayList<DesignationModel> designationModels;
    DesignationAdapter designationAdapter;
    String DeptId;
    private int popupId = 0;

    private void showDeptPopup() {

        designationAdapter = new DesignationAdapter(AddComplaintActivity.this, designationModels);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.meeting_popup, null);
        final ListView listDesign = dialogView.findViewById(R.id.listFloor);
        TextView title = dialogView.findViewById(R.id.title);
        final SearchView editTextName = dialogView.findViewById(R.id.edt);
        editTextName.setQueryHint(getString(R.string.search_here));
        editTextName.setOnQueryTextListener(this);
        title.setText(getString(R.string.select_dept));
        //Button btnUpgrade = (Button) dialogView.findViewById(R.id.btnUpgrade);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        popupId = 1;
        alertDialog.show();
        listDesign.setAdapter(designationAdapter);

        listDesign.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                DesignationModel obj = (DesignationModel) listDesign.getAdapter().getItem(position);
                edt_department.setText(obj.getDesignation());
                DeptId = obj.getId();
                alertDialog.dismiss();
            }
        });

    }
    public void getDepartmentList(String userid, String type , String branchid) {

        Singleton.getInstance().getApi().getDesignationList(userid, type ,branchid).enqueue(new Callback<DesignationRestMeta>() {
            @Override
            public void onResponse(Call<DesignationRestMeta> call, Response<DesignationRestMeta> response) {
                designationModels = response.body().getResponse();
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DesignationRestMeta> call, Throwable t) {
                progress.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        s = s.toLowerCase();
        switch (popupId) {
            case 1:
                ArrayList<DesignationModel> newlist = new ArrayList<>();
                for (DesignationModel list : designationModels) {
                    String getPurpose = list.getDesignation().toLowerCase();

                    if (getPurpose.contains(s)) {
                        newlist.add(list);
                    }
                }
                designationAdapter.filter(newlist);
                break;

        }
        return false;
    }
}



