package com.mmcs.societymaintainance.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mmcs.societymaintainance.R;
import com.mmcs.societymaintainance.activity.DrawerActivity;
import com.mmcs.societymaintainance.adaptor.HomeRecyclerAdaptor;
import com.mmcs.societymaintainance.util.Shprefrences;

public class FragmentHome extends android.support.v4.app.Fragment{
    RecyclerView rvHome;
    Shprefrences sh;
    @Override
    public void onCreate( @Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_fragment_home, container, false);
        rvHome=view.findViewById(R.id.rvItems);
        rvHome.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        HomeRecyclerAdaptor adaptor=new HomeRecyclerAdaptor(getActivity(), DrawerActivity.list);
        rvHome.setAdapter(adaptor);

        return view;
    }
}
