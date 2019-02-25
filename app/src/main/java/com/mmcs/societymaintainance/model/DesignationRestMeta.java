package com.mmcs.societymaintainance.model;

import java.util.ArrayList;

/**
 * Created by Lenovo on 20-10-2018.
 */

public class DesignationRestMeta {
    private ArrayList<DesignationModel> response;

    public ArrayList<DesignationModel> getResponse ()
    {
        return response;
    }

    public void setResponse (ArrayList<DesignationModel> response)
    {
        this.response = response;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+"]";
    }
}
