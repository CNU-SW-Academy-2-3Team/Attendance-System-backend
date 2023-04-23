package com.code.dto;

import com.code.Entity.group_tb;

public class GroupInfoResponse {

    group_tb groupInfo;
    String attendanceState;


    public GroupInfoResponse(group_tb groupInfo, String attendanceState) {
        this.groupInfo = groupInfo;
        this.attendanceState = attendanceState;
    }

    public group_tb getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(group_tb groupInfo) {
        this.groupInfo = groupInfo;
    }

    public String getAttendanceState() {
        return attendanceState;
    }

    public void setAttendanceState(String attendanceState) {
        this.attendanceState = attendanceState;
    }
}
