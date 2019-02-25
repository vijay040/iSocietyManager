package com.mmcs.societymaintainance.model;

import java.util.ArrayList;

public class LoginResMeta {

    ArrayList<LoginModel> response;
    private String code;

    private String msg;


    public ArrayList<LoginModel> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<LoginModel> response) {
        this.response = response;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
