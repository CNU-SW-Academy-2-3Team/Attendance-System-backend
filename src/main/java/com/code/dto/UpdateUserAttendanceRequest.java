package com.code.dto;

import java.time.LocalDateTime;

public class UpdateUserAttendanceRequest {


    private String guid;
    private LocalDateTime exit_time;

    public UpdateUserAttendanceRequest(String guid, LocalDateTime exit_time) {
        this.guid = guid;
        this.exit_time = exit_time;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public LocalDateTime getExit_time() {
        return exit_time;
    }

    public void setExit_time(LocalDateTime exit_time) {
        this.exit_time = exit_time;
    }
}
