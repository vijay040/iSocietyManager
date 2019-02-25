package com.mmcs.societymaintainance.model;

import java.io.Serializable;

/**
 * Created by Lenovo on 22-10-2018.
 */

public class EmployeeModel implements Serializable {
    public boolean isVisible = false;
    private String id;
    private String date;
    private String member_type;
    private String contact;
    private String email;
    private String name;
    private String pre_address;
    private String ending_date;
    private String work_detail;

    public String getEnding_date() {
        return ending_date;
    }

    public void setEnding_date(String ending_date) {
        this.ending_date = ending_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMember_type() {
        return member_type;
    }

    public void setMember_type(String member_type) {
        this.member_type = member_type;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPre_address() {
        return pre_address;
    }

    public void setPre_address(String pre_address) {
        this.pre_address = pre_address;
    }

    public String getPer_address() {
        return per_address;
    }

    public void setPer_address(String per_address) {
        this.per_address = per_address;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String per_address;
    private String nid;
    private String image;

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getWork_detail() {
        return work_detail;
    }

    public void setWork_detail(String work_detail) {
        this.work_detail = work_detail;
    }
}
