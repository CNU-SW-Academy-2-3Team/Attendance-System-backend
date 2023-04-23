package com.code.Service;

import com.code.Entity.*;
import com.code.Repository.*;
import com.code.dto.*;
import com.code.sessionAuthentication.SesseionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Bean;
//import org.apache.tomcat.util.json.JSONParser;
//import org.apache.tomcat.util.json.ParseException;
//import org.h2.util.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;

import java.util.*;

@AllArgsConstructor
@org.springframework.stereotype.Service
@Slf4j
public class AttenderService {

    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private UserAndHistoryRepository userAndHistoryRepository;
    private HistoryRepository historyRepository;
    private GroupAndUserRepository groupAndUserRepository;


    //------------------------실행 테스트-----------------------
    public List<user_tb> getUserList() {
        return userRepository.findAll();
    }

    public Optional<user_tb> dbTest(String uid) {
        return userRepository.findById(Integer.parseInt(uid));
    }


    //------------------------실행 테스트-----------------------

    //----------------------------------------------Attender API------------------------------------------------

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////      Authentication      ////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    SesseionManager sessionManager;

    // 1. 회원가입
    public String signUp(SignUpRequest signUpRequest) {

        try{
            userRepository.save(user_tb.builder()
                    .password(signUpRequest.getPassword())
                    .real_name(signUpRequest.getReal_name())
                    .email_address(signUpRequest.getEmail())
                    .nick_name(signUpRequest.getNick_name())
                    .role('U')
                    .create_date_time(LocalDateTime.now())
                    .build());

            return "200 OK";

        }
        catch(Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }

    }

    // 2. 로그인
    public String signIn(String email, String password, HttpServletResponse response) {

        var temp = userRepository.checkSignInValidation(email, password);

        try{
            if(temp != null){
                sessionManager.createSession(userRepository.findById(temp), response);
                return "200 OK";
            }
            else{
                return "Not a Member";
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    // 3. 로그아웃
    public String signOut(HttpServletResponse response, HttpServletRequest request) {

        try{
            sessionManager.expire(request);
            return "200 OK";
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    //4. 로그인 체크
    private String authenticationCheck(HttpServletRequest request) {
        Optional<user_tb> member = (Optional<user_tb>) sessionManager.getSession(request);

        if (member == null) {
            return "NOT";
        }
        return "OK";
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //API1 : 사용자가 생성한 그룹 리스트 가져오기
    public List<group_tb> getGroupList(HttpServletRequest request, HttpServletResponse response, Integer uid) throws IOException {

        //로그인 체크 과정 수행
        if(authenticationCheck(request).equals("OK")){
            try{
                return groupRepository.getGroupList(uid);
            }catch(Exception e){
                log.info(e.getMessage());
                throw new RuntimeException();
            }
        }
        else{
            response.sendError(401);
            return null;
        }

    }

    //API2 : 그룹 생성 & 초대코드 return
    public String createGroup(HttpServletRequest request, HttpServletResponse response, Integer uid, String groupTitle, String groupDetail) throws IOException {

        if(authenticationCheck(request).equals("OK")){
            try{
                String inviteCode = UUID.randomUUID().toString().substring(0, 6);; //랜덤 문자열 생성 : 초대코드
                groupRepository.createGroup(inviteCode, uid, groupTitle, groupDetail);   //그룹 생성
                return groupRepository.getInviteCode(); //초대코드 반환

            }catch(Exception e){
                log.info(e.getMessage());
                throw new RuntimeException();
            }
        }
        else{
            response.sendError(401);
            return null;
        }
    }

    //API3 : 접속한 그룹 정보 조회 & 출석 정보 포함
    public GroupInfoResponse getGroupInfo(HttpServletRequest request, HttpServletResponse response, Integer gid, Integer uid) throws IOException {

        if(authenticationCheck(request).equals("OK")){
            try{
                group_tb groupInfo = groupRepository.getGroupInfo(gid);
                user_and_history_tb attendanceState = userAndHistoryRepository.getAttendanceState(gid, uid);

                if(attendanceState == null){
                    return new GroupInfoResponse(groupInfo, "NO");
                }
                else{

                    history_tb userHistory = historyRepository.getHistoryState(attendanceState.getHid());

                    if(userHistory.getExit_time() == null){
                        return new GroupInfoResponse(groupInfo, "Enter");
                    }
                    else{
                        return new GroupInfoResponse(groupInfo, "Exit");
                    }

                }

            }catch(Exception e){
                log.info(e.getMessage());
                throw new RuntimeException();
            }
        }
        else{
            response.sendError(401);
            return null;
        }
    }

    //API4 : 출석 코드 생성
    public void putAttendanceCode(HttpServletRequest request, HttpServletResponse response, Integer gid, LocalDateTime acceptStartTime, LocalDateTime acceptEndTime) throws IOException {

        if(authenticationCheck(request).equals("OK")){
            try{

                String attendanceCode = RandomStringUtils.randomAlphabetic(10); //랜덤 문자열 생성 : 출석코드

                groupRepository.insertAttendanceCode(attendanceCode, acceptStartTime, acceptEndTime);
                groupRepository.putGroupCid(gid);

                response.setStatus(200);

            }catch(Exception e){
                log.info(e.getMessage());
                throw new RuntimeException();
            }

        }
        else{
            response.sendError(401);
        }
    }

    //API5 : 출석 코드 조회
    public String getAttendanceCode(HttpServletRequest request, HttpServletResponse response, Integer gid) throws IOException {

        if(authenticationCheck(request).equals("OK")){

            try{
                return groupRepository.getAttendanceCode(gid);
            }catch(Exception e){
                log.info(e.getMessage());
                throw new RuntimeException();
            }

        }
        else{
            response.sendError(401);
            return null;
        }
    }

    //API6 : 자신이 참여한 그룹 리스트 조회
    public List<GetJoinedGroupResponse> getJoinedGroupList(HttpServletRequest request, HttpServletResponse response, Integer uid) throws IOException {

        if(authenticationCheck(request).equals("OK")){

            try {

                List<group_and_user_tb> joinedGroupList = groupAndUserRepository.getJoinedGroupList(uid);

                ArrayList<GetJoinedGroupResponse> result = new ArrayList<GetJoinedGroupResponse>();

                for (int i = 0; i < joinedGroupList.size(); i++) {
                    group_and_user_tb groupAndUserInfo = joinedGroupList.get(i);
                    result.add(new GetJoinedGroupResponse(
                            groupAndUserInfo.getGuid(),
                            groupRepository.getJoinedGroupInfo(groupAndUserInfo.getGid())
                    ));
                }

                return result;
            }
            catch(Exception e){
                log.info(e.getMessage());
                throw new RuntimeException();
            }
        }
        else{
            response.sendError(401);
            return null;
        }
    }

    //API7 : 메인페이지 - 전체 회원수, 그룹수, 오늘 출석한 사람 수
    public MainPageResponse getMainPageInfo() {

        try {

            return new MainPageResponse(
                    groupRepository.getAllMemberConut(),
                    groupRepository.getAllGroupCount(),
                    groupRepository.getTodayAttendCount()
            );
        }
        catch(Exception e){
            log.info(e.getMessage());
            throw new RuntimeException();
        }
    }

    //API8 : 사용자의 출석 상태 Insert
    public String insertUserAttendance(HttpServletRequest request, HttpServletResponse response, String guid, LocalDateTime enterTime, String attendanceCode) throws IOException {

        if(authenticationCheck(request).equals("OK")){
            //0. cid와 일치하는지 유효성 검사 코드를 작성한다.

            try {

                Integer groupCid = groupRepository.getGroupCurrentCid(guid);
                Integer foundCid = groupRepository.getFoundCid(attendanceCode);

                if(groupCid.compareTo(foundCid) == 0) { //올바른 코드가 입력되었다면

                    //코드의 입력시간이 유효한지 확인한다.
                    LocalDateTime acceptStartTime = groupRepository.getAcceptStartTime(foundCid);
                    LocalDateTime acceptEndTime = groupRepository.getAcceptEndTime(foundCid);

                    if(acceptStartTime.isBefore(acceptEndTime) &&
                            acceptStartTime.isBefore(enterTime) &&
                            acceptEndTime.isAfter(enterTime))
                    {
                        //1. history_tb에 출석정보 추가
                        groupRepository.insertUserAttendanceToHistory(guid, enterTime, groupCid);
                        //2. user_and_history_tb에 출석 정보 추가
                        groupRepository.insertUserAttendanceToUAH(guid);

                        return "200 OK";
                    }
                    else{
                        return "Not Valid Time";
                    }
                }
                else{
                    return "Not Valid Code"; // 올바르지 않은 코드가 입력되었다면.
                }

            }
            catch(Exception e){
                log.info(e.getMessage());
                throw new RuntimeException();
            }
        }
        else{
            response.sendError(401);
            return null;
        }
    }

    //API9 : 사용자의 출석 상태 Update
    public void updateUserAttendance(HttpServletRequest request, HttpServletResponse response, String guid, LocalDateTime exitTime) throws IOException {

        if(authenticationCheck(request).equals("OK")){
            try {
                groupRepository.updateUserAttendance(guid, exitTime);
                response.setStatus(200);
            }
            catch(Exception e){
                log.info(e.getMessage());
                throw new RuntimeException();
            }
        }
        else{
            response.sendError(401);
        }

    }

    //API10 : 그룹 참가
    public String insertGroupUser(HttpServletRequest request, HttpServletResponse response, Integer uid, String userCode) throws IOException {

        if(authenticationCheck(request).equals("OK")){
            //0. 사용자가 입력한 초대 코드가 유효한 코드인지 확인한다.
            //1. 사용자가 입력한 초대 코드가 유효하지 않다면 FAIL

            try{
                Integer foundGid = groupRepository.getGroupInviteCode(userCode);

                if(foundGid != null) { //올바른 코드가 입력되었다면

                    //1.5 이미 사용자가 그룹에 속한 사람이라면
                    if(groupRepository.getIsUserInGroup(uid, foundGid) != null){
                        return "Already Exist";
                    }
                    else{
                        //2. 사용자가 입력한 초대 코드가 유효하다면 GROUP USER에 INSERT
                        groupRepository.insertGroupUser(uid, foundGid);

                        return "200 OK";
                    }
                }
                else{
                    //1. 사용자가 입력한 초대 코드가 유효하지 않다면 FAIL
                    return "Not Valid Invite Code"; // 올바르지 않은 코드가 입력되었다면.
                }
            }catch(Exception e){
                log.info(e.getMessage());
                throw new RuntimeException();
            }

        }
        else{
            response.sendError(401);
            return null;
        }
    }

    //API11 : 그룹의 회원수
    public Integer getGroupUserCount(HttpServletRequest request, HttpServletResponse response, Integer gid) throws IOException {

        if(authenticationCheck(request).equals("OK")){

            try{
                return groupRepository.getGroupUserCount(gid);
            }
            catch(Exception e){
                throw new RuntimeException();
            }

        }
        else{
            response.sendError(401);
            return null;
        }
    }

    //API12 : 그룹 참여자의 현재 출석 정보 조회
    public List<GetGroupMembersAttendanceStateResponse> getGroupMembersAttendanceState(HttpServletRequest request, HttpServletResponse response, Integer gid) throws IOException {

        if(authenticationCheck(request).equals("OK")){

            try{
                List<group_and_user_tb> getJoinedMemberList = groupAndUserRepository.getJoinedMemberList(gid);
                List<GetGroupMembersAttendanceStateResponse> result = new LinkedList<>();

                for(int i=0; i<getJoinedMemberList.size(); i++){

                    Integer guid = getJoinedMemberList.get(i).getGuid();
                    Integer uid = getJoinedMemberList.get(i).getUid();
                    user_tb userInfo = userRepository.getUserInfo(uid);

                    Integer nowHid = userAndHistoryRepository.getGroupMemberNowState(guid);

                    if(nowHid != null){

                        history_tb userNowHistory = historyRepository.getHistoryState(nowHid);

                        if(userNowHistory.getExit_time() == null && userNowHistory.getAttendance_state() == null){
                            result.add(GetGroupMembersAttendanceStateResponse
                                    .builder()
                                    .user(userInfo)
                                    .state("Enter")
                                    .build());
                        }
                        else{
                            result.add(GetGroupMembersAttendanceStateResponse
                                    .builder()
                                    .user(userInfo)
                                    .state("Exit")
                                    .build());
                        }

                    }
                    else{
                        result.add(GetGroupMembersAttendanceStateResponse
                                .builder()
                                .user(userInfo)
                                .state("Not Entered")
                                .build());

                    }
                }

                return result;

            }catch(Exception e){
                log.info(e.getMessage());
                throw new RuntimeException();
            }
        }
        else{
            response.sendError(401);
            return null;
        }

    }

    //----------------------------------------------Attender API------------------------------------------------
}
