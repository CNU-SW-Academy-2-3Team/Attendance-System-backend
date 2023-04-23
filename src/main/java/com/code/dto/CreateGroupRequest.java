package com.code.dto;

import lombok.AllArgsConstructor;


@AllArgsConstructor

public class CreateGroupRequest {

    private Integer uid;
    private String group_detail;
    private String group_title;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer UID) {
        this.uid = UID;
    }

    public String getGroup_detail() {
        return group_detail;
    }

    public void setGroup_detail(String group_detail) {
        this.group_detail = group_detail;
    }

    public String getGroup_title() {
        return group_title;
    }

    public void setGroup_title(String group_title) {
        this.group_title = group_title;
    }


}
