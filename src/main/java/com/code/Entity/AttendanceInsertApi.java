package com.code.Entity;

import java.time.LocalDateTime;

public class AttendanceInsertApi {

    private String guid;
    private LocalDateTime enter_time;
    private String attendance_code;


    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public LocalDateTime getEnter_time() {
        return enter_time;
    }

    public void setEnter_time(LocalDateTime enter_time) {
        this.enter_time = enter_time;
    }

    public String getAttendance_code() {
        return attendance_code;
    }

    public void setAttendance_code(String attendance_code) {
        this.attendance_code = attendance_code;
    }
}
