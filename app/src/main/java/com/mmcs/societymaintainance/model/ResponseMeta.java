package com.mmcs.societymaintainance.model;

import java.util.ArrayList;

public class ResponseMeta {
    private ArrayList<FloorModel> response;

    public ArrayList<FloorModel> getResponse ()
    {
        return response;
    }

    public void setResponse (ArrayList<FloorModel> response)
    {
        this.response = response;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+"]";
    }
}


