package com.mmcs.societymaintainance.model;

public class AttandenceModel {

    private String login_date_time;

    private String status;

    private String logout_location;

    private String login_location;

    private String logout_date_time;

    public String getLogin_date_time ()
    {
        return login_date_time;
    }

    public void setLogin_date_time (String login_date_time)
    {
        this.login_date_time = login_date_time;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getLogout_location ()
    {
        return logout_location;
    }

    public void setLogout_location (String logout_location)
    {
        this.logout_location = logout_location;
    }

    public String getLogin_location ()
    {
        return login_location;
    }

    public void setLogin_location (String login_location)
    {
        this.login_location = login_location;
    }

    public String getLogout_date_time ()
    {
        return logout_date_time;
    }

    public void setLogout_date_time (String logout_date_time)
    {
        this.logout_date_time = logout_date_time;
    }


}

