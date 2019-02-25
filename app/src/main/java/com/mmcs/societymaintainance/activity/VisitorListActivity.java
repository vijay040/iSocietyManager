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
import com.mmcs.societymaintainance.adaptor.VisitorAdapter;
import com.mmcs.societymaintainance.model.LoginModel;
import com.mmcs.societymaintainance.model.VisitorModel;
import com.mmcs.societymaintainance.model.VisitorRestMeta;
import com.mmcs.societymaintainance.util.Shprefrences;
import com.mmcs.societymaintainance.util.Singleton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitorListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ListView listVisitor;
    ProgressBar progressBar;
    RelativeLayout txtAdd;
    ArrayList<VisitorModel> visitorModels = new ArrayList();
    Shprefrences sh;
    VisitorAdapter visitorAdapter;
    LoginModel loginModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_list);
        listVisitor = findViewById(R.id.listVisitor);
        progressBar = findViewById(R.id.progress);
        SearchView editTextName = (SearchView) findViewById(R.id.edt);
        editTextName.setQueryHint(getString(R.string.search_here));
        editTextName.setOnQueryTextListener(this);


        txtAdd = findViewById(R.id.txtAdd);
        sh = new Shprefrences(this);
        loginModel = sh.getLoginModel(getResources().getString(R.string.login_model));
        String type = sh.getString("TYPE", "");
        if (type.equalsIgnoreCase("Owner") || type.equalsIgnoreCase("Renter"))
            txtAdd.setVisibility(View.GONE);

        txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VisitorListActivity.this, AddVisitorActivity.class));
            }
        });

        listVisitor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                VisitorAdapter adapter = (VisitorAdapter) adapterView.getAdapter();
                VisitorModel model = adapter.list.get(i);
                Intent intent = new Intent(VisitorListActivity.this, VisitorDetailActivity.class);
                intent.putExtra(getString(R.string.visitor_model), model);
                startActivity(intent);
            }
        });

        setTitle();
        back();
    }

    private void setTitle() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.visitor_list));
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

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        getVisitors(loginModel.getId(), loginModel.getType(), loginModel.getBranch_id());
    }

    public void getVisitors(String userid, String type, String branchid) {

        Singleton.getInstance().getApi().getVisitorList(userid, type, branchid).enqueue(new Callback<VisitorRestMeta>() {
            @Override
            public void onResponse(Call<VisitorRestMeta> call, Response<VisitorRestMeta> response) {
                if (response.body() == null)
                    return;
                visitorModels = response.body().getResponse();
                visitorAdapter = new VisitorAdapter(VisitorListActivity.this, visitorModels);
                listVisitor.setAdapter(visitorAdapter);
                listVisitor.setEmptyView(findViewById(R.id.imz_nodata));
                if(visitorModels.size()>0)
                    listVisitor.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<VisitorRestMeta> call, Throwable t) {
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
        ArrayList<VisitorModel> newlist = new ArrayList<>();
        for (VisitorModel filterlist : visitorModels) {
            String name = filterlist.getName().toLowerCase();
            String address = filterlist.getAddress().toLowerCase();
            String floor = filterlist.getFloor_no().toLowerCase();
            String unit = filterlist.getUnit().toLowerCase();
            String mob = filterlist.getMobile().toLowerCase();

            s = s.replaceAll("\\s+", "");
            name = name.replaceAll("\\s+", "");
            address = address.replaceAll("\\s+", "");
            floor = floor.replaceAll("\\s+", "");
            unit = unit.replaceAll("\\s+", "");
            mob = mob.replaceAll("\\s+", "");

           // if (name.contains(s) || address.contains(s) || floor.contains(s) || unit.contains(s) || mob.contains(s)) {
            if (name.contains(s) || address.contains(s)||  unit.contains(s) || mob.contains(s)) {
                newlist.add(filterlist);
            }
        }
        if(newlist.size()>0)
        visitorAdapter.filter(newlist);
        return true;
    }


}
