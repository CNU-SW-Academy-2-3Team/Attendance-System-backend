package com.code.dto;

import com.code.Entity.group_tb;
import com.code.Entity.user_tb;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.h2.engine.User;


@Setter
@Getter
@Builder
public class GetGroupMembersAttendanceStateResponse {

    private user_tb user;
    private String state;


}
