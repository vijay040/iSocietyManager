package com.mmcs.societymaintainance.model;

public class SettingsModel {
    private String register_enabled;

    public String getRegister_enabled ()
    {
        return register_enabled;
    }

    public void setRegister_enabled (String register_enabled)
    {
        this.register_enabled = register_enabled;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [register_enabled = "+register_enabled+"]";
    }
}
