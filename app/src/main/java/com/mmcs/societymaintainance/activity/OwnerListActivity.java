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
import com.mmcs.societymaintainance.adaptor.OwnerAdapter;
import com.mmcs.societymaintainance.model.LoginModel;
import com.mmcs.societymaintainance.model.OwnerModel;
import com.mmcs.societymaintainance.model.OwnerRestMeta;
import com.mmcs.societymaintainance.util.Shprefrences;
import com.mmcs.societymaintainance.util.Singleton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ListView listOwner;
    ProgressBar progressBar;
    RelativeLayout txtAdd;
    ArrayList<OwnerModel> ownerModels = new ArrayList();
    Shprefrences sh;
    OwnerAdapter ownerAdapter;
    LoginModel loginModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_list);
        listOwner = findViewById(R.id.listOwner);
        progressBar = findViewById(R.id.progress);
        txtAdd = findViewById(R.id.txtAdd);
        sh = new Shprefrences(this);
        loginModel = sh.getLoginModel(getResources().getString(R.string.login_model));
        SearchView editTextName = (SearchView) findViewById(R.id.edt);
        editTextName.setQueryHint(getString(R.string.search_here));
        editTextName.setOnQueryTextListener(this);
        txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OwnerListActivity.this, AddOwnerActivity.class));
            }
        });
        listOwner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OwnerAdapter adapter = (OwnerAdapter) adapterView.getAdapter();
                OwnerModel model = adapter.list.get(i);
                Intent intent = new Intent(OwnerListActivity.this, OwnerDetailActivity.class);
                intent.putExtra(getString(R.string.owner_model), model);
                startActivity(intent);
            }
        });
        setTitle();
        back();
    }

    private void setTitle() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.owner_list));

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
        getOwnerList(loginModel.getId(), loginModel.getType(), loginModel.getBranch_id());
    }

    public void getOwnerList(String userid, String type, String branchid) {

        Singleton.getInstance().getApi().getOwnerList(userid, type, branchid).enqueue(new Callback<OwnerRestMeta>() {
            @Override
            public void onResponse(Call<OwnerRestMeta> call, Response<OwnerRestMeta> response) {
                if (response.body() == null)
                    return;
                ownerModels = response.body().getResponse();
                ownerAdapter = new OwnerAdapter(OwnerListActivity.this, ownerModels);
                listOwner.setAdapter(ownerAdapter);
                listOwner.setEmptyView(findViewById(R.id.imz_nodata));

                if (ownerModels.size() > 0)
                    listOwner.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<OwnerRestMeta> call, Throwable t) {
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
        ArrayList<OwnerModel> newlist = new ArrayList<>();
        for (OwnerModel filterlist : ownerModels) {
            String name = filterlist.getName().toLowerCase();
            String unit = filterlist.getUnit().toLowerCase();
            String mob = filterlist.getContact().toLowerCase();
            String email = filterlist.getContact().toLowerCase();

            s = s.replaceAll("\\s+", "");
            name = name.replaceAll("\\s+", "");
            unit = unit.replaceAll("\\s+", "");
            mob = mob.replaceAll("\\s+", "");


            if (name.contains(s) || mob.contains(s) || email.equals(s) || unit.contains(s)) {
                newlist.add(filterlist);
            }
        }
        if (ownerAdapter != null)
            ownerAdapter.filter(newlist);
        return true;
    }
}
