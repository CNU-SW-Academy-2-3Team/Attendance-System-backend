package com.code.dto;

public class MainPageResponse {

    Integer memberCount;
    Integer groupCount;
    Integer todayAttendCount;


    public MainPageResponse(Integer memberCount, Integer groupCount, Integer todayAttendCount) {
        this.memberCount = memberCount;
        this.groupCount = groupCount;
        this.todayAttendCount = todayAttendCount;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public Integer getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(Integer groupCount) {
        this.groupCount = groupCount;
    }

    public Integer getTodayAttendCount() {
        return todayAttendCount;
    }

    public void setTodayAttendCount(Integer todayAttendCount) {
        this.todayAttendCount = todayAttendCount;
    }
}
