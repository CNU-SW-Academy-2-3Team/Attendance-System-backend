package com.code.dto;

import com.code.Entity.group_tb;

public class GetJoinedGroupResponse {

    private Integer guid;
    private group_tb group;

    public Integer getGuid() {
        return guid;
    }

    public void setGuid(Integer guid) {
        this.guid = guid;
    }

    public group_tb getGroup() {
        return group;
    }

    public void setGroup(group_tb group) {
        this.group = group;
    }


    public GetJoinedGroupResponse(Integer guid, group_tb group) {
        this.guid = guid;
        this.group = group;
    }
}
