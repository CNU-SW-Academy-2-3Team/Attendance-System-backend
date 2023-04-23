package com.code.Controller;

import com.code.Entity.*;
import com.code.dto.*;
import com.code.Service.AttenderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
public class Controller {

    private final AttenderService attenderService;

    //------------------------실행 테스트-----------------------
    @GetMapping(value = "/api/test")
    public String test() {
        return "{name : 'icecream'}";
    }

    @GetMapping(value = "/api/dbTest/{uid}")
    public Optional<user_tb> dbTest(@PathVariable String uid) {
        return attenderService.dbTest(uid);
    }

    //------------------------실행 테스트-----------------------


    //----------------------------------------------Attender API------------------------------------------------

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////      Authentication      ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 1. 회원가입
    @PostMapping(value = "/api/user/signup")
    public String signUp(@RequestBody SignUpRequest signUpRequest) {
        return attenderService.signUp(signUpRequest);
    }

    // 2. 로그인
    @PostMapping(value = "/api/user/signin")
    public String signIn(@RequestBody SignInRequest signInRequest, HttpServletResponse response) {
        return attenderService.signIn(signInRequest.getEmail(), signInRequest.getPassword(), response);
    }

    // 3. 로그아웃
    @PostMapping("/api/user/signout")
    public String signOut(HttpServletResponse response, HttpServletRequest request) {
        return attenderService.signOut(response, request);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //API1 : 사용자가 생성한 그룹 리스트 가져오기
    @GetMapping(value = "/api/user/{uid}/groups/created")
    public List<group_tb> getGroupList(@PathVariable String uid, HttpServletRequest request, HttpServletResponse response) {
        try{
            return attenderService.getGroupList(request, response, Integer.parseInt(uid));
        }
        catch(Exception e){
            log.info(e.getMessage());
            return null;
        }
    }

    //API2 : 그룹 생성 & 초대코드 return
    @PostMapping(value = "/api/group")
    public String createGroup(@RequestBody CreateGroupRequest createGroupRequest, HttpServletRequest request, HttpServletResponse response) {

        try{
            return attenderService.createGroup(request, response, createGroupRequest.getUid(), createGroupRequest.getGroup_title(), createGroupRequest.getGroup_detail());
        }
        catch(Exception e){
            log.info(e.getMessage());
            return null;
        }
    }

    //API3 : 접속한 그룹 정보 조회 & 출석 정보 포함
    @GetMapping(value = "/api/group/{gid}/{uid}")
    public GroupInfoResponse getGroupInfo(@PathVariable String gid, @PathVariable String uid, HttpServletRequest request, HttpServletResponse response) {
        try{
            return attenderService.getGroupInfo(request, response, Integer.parseInt(gid), Integer.parseInt(uid));
        }
        catch(Exception e){
            log.info(e.getMessage());
            return null;
        }
    }

    //API4 : 출석 코드 생성
    @PutMapping(value = "/api/attendance")
    public void putAttendanceCode(@RequestBody PutAttendanceCodeRequest putAttendanceCodeRequest, HttpServletRequest request, HttpServletResponse response) {
        try{
            attenderService.putAttendanceCode(request, response, putAttendanceCodeRequest.getGid(), putAttendanceCodeRequest.getAcceptStartTime(), putAttendanceCodeRequest.getAcceptEndTime());
        }
        catch(Exception e){
            log.info(e.getMessage());
        }
    }

    //API5 : 출석 코드 조회
    @GetMapping(value = "/api/group/{gid}/attendance")
    public String getAttendanceCode(@PathVariable String gid, HttpServletRequest request, HttpServletResponse response) {
        try {
            return attenderService.getAttendanceCode(request, response, Integer.parseInt(gid));
        }
        catch(Exception e){
            log.info(e.getMessage());
            return null;

        }
    }

    //API6 : 자신이 참여한 그룹 리스트 조회
    @GetMapping(value = "/api/user/{uid}/groups/joined")
    public List<GetJoinedGroupResponse> getJoinedGroupList(@PathVariable String uid, HttpServletRequest request, HttpServletResponse response) {
        try {
            return attenderService.getJoinedGroupList(request, response, Integer.parseInt(uid));
        }
        catch(Exception e){
            log.info(e.getMessage());
            return null;

        }
    }

    //API7 : 메인페이지 - 전체 회원수, 그룹수, 오늘 출석한 사람 수
    @GetMapping(value = "/api/main")
    public MainPageResponse getMainPageInfo() {
        return attenderService.getMainPageInfo();
    }

    //API8 : 사용자의 출석 상태 Insert
    @PostMapping(value = "/api/user/attendance")
    public String insertUserAttendance(@RequestBody InsertUserAttendanceRequest insertUserAttendanceRequest, HttpServletRequest request, HttpServletResponse response) {
        try {
            return attenderService.insertUserAttendance(request, response, insertUserAttendanceRequest.getGuid(), insertUserAttendanceRequest.getEnter_time(), insertUserAttendanceRequest.getAttendance_code());
        }
        catch(Exception e){
            log.info(e.getMessage());
            return null;

        }
    }

    //API9 : 사용자의 출석 상태 Update
    @PatchMapping(value = "/api/user/attendance")
    public void updateUserAttendance(@RequestBody UpdateUserAttendanceRequest updateUserAttendanceRequest, HttpServletRequest request, HttpServletResponse response) {
        try {
            attenderService.updateUserAttendance(request, response, updateUserAttendanceRequest.getGuid(), updateUserAttendanceRequest.getExit_time());

        }
        catch(Exception e){
            log.info(e.getMessage());
        }
    }

    //API10 : 그룹 참가
    @PostMapping(value = "/api/group/join")
    public String insertGroupUser(@RequestBody InsertGroupUserRequest insertGroupUserRequest, HttpServletRequest request, HttpServletResponse response) {
        try {
            return attenderService.insertGroupUser(request, response, insertGroupUserRequest.getUid(), insertGroupUserRequest.getInvite_code());
        }
        catch(Exception e){
            log.info(e.getMessage());
            return null;
        }
    }

    //API11 : 그룹의 회원수
    @GetMapping(value = "/api/group/{gid}/count")
    public Integer getGroupUserCount(@PathVariable String gid, HttpServletRequest request, HttpServletResponse response) {
        try {
            return attenderService.getGroupUserCount(request, response, Integer.parseInt(gid));
        }
        catch(Exception e){
            log.info(e.getMessage());
            return null;
        }
    }

    //API12 : 그룹 참여자의 현재 출석 정보 조회
    @GetMapping(value = "/api/group/{gid}")
    public List<GetGroupMembersAttendanceStateResponse> getGroupMembersAttendanceState(@PathVariable String gid, HttpServletRequest request, HttpServletResponse response) {

        try {
            return attenderService.getGroupMembersAttendanceState(request, response, Integer.parseInt(gid));
        }
        catch(Exception e){
            log.info(e.getMessage());
            return null;
        }
    }

    //----------------------------------------------Attender API------------------------------------------------

}
