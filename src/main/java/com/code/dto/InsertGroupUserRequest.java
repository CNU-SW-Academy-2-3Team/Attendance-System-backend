package com.code.dto;

public class InsertGroupUserRequest {

    private Integer uid;

    private String invite_code;


    public InsertGroupUserRequest(Integer uid, String invite_code) {
        this.uid = uid;
        this.invite_code = invite_code;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String attendance_code) {
        this.invite_code = attendance_code;
    }



}
