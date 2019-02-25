package com.mmcs.societymaintainance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.mmcs.societymaintainance.R;
import com.mmcs.societymaintainance.adaptor.ComplaintAdapter;
import com.mmcs.societymaintainance.model.ComplaintModel;
import com.mmcs.societymaintainance.model.ComplaintRestMeta;
import com.mmcs.societymaintainance.model.LoginModel;
import com.mmcs.societymaintainance.util.Shprefrences;
import com.mmcs.societymaintainance.util.Singleton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplaintListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ListView listComplaint;
    ProgressBar progressBar;
    RelativeLayout txtAdd;
    ArrayList<ComplaintModel> complaintModels = new ArrayList();
    Shprefrences sh;
    ComplaintAdapter complaintAdapter;
    LoginModel loginModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_list);
        listComplaint = findViewById(R.id.listComplaint);
        progressBar = findViewById(R.id.progress);
        sh = new Shprefrences(this);
        SearchView editTextName = (SearchView) findViewById(R.id.edt);
        editTextName.setQueryHint(getString(R.string.search_here));
        editTextName.setOnQueryTextListener(this);
        loginModel = sh.getLoginModel(getResources().getString(R.string.login_model));
        txtAdd = findViewById(R.id.txtAdd);
        if (loginModel.getType().equalsIgnoreCase("1") || loginModel.getType().equalsIgnoreCase("3"))
            txtAdd.setVisibility(View.GONE);
        back();
        setTitle();
        listComplaint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ComplaintAdapter adapter = (ComplaintAdapter) adapterView.getAdapter();
                ComplaintModel model = adapter.list.get(i);
                Intent intent = new Intent(ComplaintListActivity.this, ComplaintDetailActivity.class);
                intent.putExtra(getString(R.string.comp_model), model);
                startActivity(intent);
            }
        });
        txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ComplaintListActivity.this, AddComplaintActivity.class));
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
        title.setText(getString(R.string.complaint_list));
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        getComplaintList(loginModel.getId(), loginModel.getType(), loginModel.getBranch_id());
    }

    public void getComplaintList(String userid, String type, String branchid) {

        Singleton.getInstance().getApi().getComplaintList(userid, type, branchid, loginModel.getFloor_no(), loginModel.getUnit_no()).enqueue(new Callback<ComplaintRestMeta>() {
            @Override
            public void onResponse(Call<ComplaintRestMeta> call, Response<ComplaintRestMeta> response) {
                if (response.body() == null)
                    return;
                complaintModels = response.body().getResponse();
                complaintAdapter = new ComplaintAdapter(ComplaintListActivity.this, complaintModels);
                listComplaint.setAdapter(complaintAdapter);
                listComplaint.setEmptyView(findViewById(R.id.imz_nodata));
                if(complaintModels.size()>0)
                    listComplaint.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ComplaintRestMeta> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
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
        ArrayList<ComplaintModel> newlist = new ArrayList<>();
        for (ComplaintModel filterlist : complaintModels) {
            String title = filterlist.getTitle().toLowerCase();
            String des = filterlist.getC_description().toLowerCase();
            String date = filterlist.getDate().toLowerCase();
            String dept = filterlist.getDepartment().toLowerCase();
            String status = filterlist.getStatus().toLowerCase();
            String unit = filterlist.getUnit().toLowerCase();
            s = s.replaceAll("\\s+", "");
            title = title.replaceAll("\\s+", "");
            date = date.replaceAll("\\s+", "");
            unit = unit.replaceAll("\\s+", "");

            if (title.contains(s) || des.contains(s) || date.contains(s) || dept.contains(s) || status.contains(s) || unit.contains(s)) {
                newlist.add(filterlist);
            }
        }
        if(newlist.size()>0)
        complaintAdapter.filter(newlist);
        return true;
    }
}
