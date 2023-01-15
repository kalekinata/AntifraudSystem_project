package com.system.antifraud.model.request;

import com.google.gson.annotations.SerializedName;

public class CheckRequest {
    public String checkid;
    public String trid;
    public String status_tr;

    public CheckRequest() {
    }

    public CheckRequest(String checkid, String trid, String status_tr) {
        this.checkid = checkid;
        this.trid = trid;
        this.status_tr = status_tr;
    }

    public String getCheckid() {
        return checkid;
    }

    public void setCheckid(String checkid) {
        this.checkid = checkid;
    }

    public String getTrid() {
        return trid;
    }

    public void setTrid(String trid) {
        this.trid = trid;
    }

    public String getStatus_tr() {
        return status_tr;
    }

    public void setStatus_tr(String status_tr) {
        this.status_tr = status_tr;
    }
}
