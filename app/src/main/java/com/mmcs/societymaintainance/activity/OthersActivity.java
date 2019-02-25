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
import com.mmcs.societymaintainance.adaptor.EmployeeAdapter;
import com.mmcs.societymaintainance.model.EmployeeModel;
import com.mmcs.societymaintainance.model.EmployeeRestMeta;
import com.mmcs.societymaintainance.model.LoginModel;
import com.mmcs.societymaintainance.util.Shprefrences;
import com.mmcs.societymaintainance.util.Singleton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OthersActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ListView listEmployee;
    ProgressBar progressBar;
    Shprefrences sh;
    ArrayList<EmployeeModel> employeeModels=new ArrayList();
    EmployeeAdapter employeeAdapter;
    RelativeLayout txtAdd;
    LoginModel loginModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        listEmployee=findViewById(R.id.listEmployee);
        progressBar=findViewById(R.id.progress);
        txtAdd=findViewById(R.id.txtAdd);
        SearchView editTextName=(SearchView) findViewById(R.id.edt);
        editTextName.setQueryHint(getString(R.string.search_here));
        editTextName.setOnQueryTextListener(this);
        sh=new Shprefrences(this);
        loginModel=sh.getLoginModel(getResources().getString(R.string.login_model));
        setTitle();
        back();
        progressBar.setVisibility(View.VISIBLE);
      //  getEmployee(loginModel.getId(),loginModel.getType(),loginModel.getBranch_id());
        txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OthersActivity.this,AddOthersActivity.class);
                startActivity(intent);
            }
        });
        listEmployee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EmployeeAdapter adapter = (EmployeeAdapter) adapterView.getAdapter();
                EmployeeModel model = adapter.list.get(i);
                Intent intent = new Intent(OthersActivity.this, OthersDetailActivity.class);
                intent.putExtra(getString(R.string.driver_model), model);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        getEmployee(loginModel.getId(),loginModel.getType(),loginModel.getBranch_id());
    }

    private void setTitle() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("Others");
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

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        s=s.toLowerCase();
        ArrayList<EmployeeModel> newlist=new ArrayList<>();
        for(EmployeeModel filterlist:employeeModels)
        {
            String name=filterlist.getName().toLowerCase();
            String address =filterlist.getPre_address().toLowerCase();
            String email =filterlist.getEmail().toLowerCase();
            String desi =filterlist.getMember_type().toLowerCase();
            String mob =filterlist.getContact().toLowerCase();
            if(name.contains(s)||address.contains(s)||email.contains(s)||desi.contains(s)||mob.contains(s)) {
                newlist.add(filterlist);
            }
        }
        if(newlist.size()>0)
        employeeAdapter.filter(newlist);
        return true;
    }

    public void getEmployee(String userid, String type , String branchid) {

        Singleton.getInstance().getApi().getOthersList(userid,type ,branchid).enqueue(new Callback<EmployeeRestMeta>() {
            @Override
            public void onResponse(Call<EmployeeRestMeta> call, Response<EmployeeRestMeta> response) {
                if(response.body()==null)
                    return;
                employeeModels=response.body().getResponse();

                employeeAdapter=new EmployeeAdapter(OthersActivity.this,employeeModels);
                listEmployee.setAdapter(employeeAdapter);
                listEmployee.setEmptyView(findViewById(R.id.imz_nodata));
                if(employeeModels.size()>0)
                    listEmployee.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<EmployeeRestMeta> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
