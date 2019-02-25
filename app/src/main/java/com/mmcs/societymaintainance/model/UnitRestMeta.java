package com.mmcs.societymaintainance.model;

import java.util.ArrayList;

/**
 * Created by Lenovo on 20-10-2018.
 */

public class UnitRestMeta {
    private ArrayList<UnitModel> response;

    public ArrayList<UnitModel> getResponse ()
    {
        return response;
    }

    public void setResponse (ArrayList<UnitModel> response)
    {
        this.response = response;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+"]";
    }
}
