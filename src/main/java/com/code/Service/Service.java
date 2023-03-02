package com.code.Service;

import com.code.Entity.API6Response;
import com.code.Entity.User;
import com.code.Repository.GroupRepository;
import com.code.Entity.Group;
import com.code.Repository.UserRepository;
import lombok.AllArgsConstructor;

import org.apache.commons.lang3.RandomStringUtils;
//import org.apache.tomcat.util.json.JSONParser;
//import org.apache.tomcat.util.json.ParseException;
//import org.h2.util.json.JSONObject;
import org.springframework.cglib.core.Local;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@org.springframework.stereotype.Service
public class Service {

    private UserRepository userRepository;
    private GroupRepository groupRepository;

    //------------------------실행 테스트-----------------------
    public List<User> getUserList() {
        return userRepository.findAll();
    }

    public String getUser(Integer userId) {
        return "{\n" +
                "\t\t\"id\": 1,\n" +
                "\t\t\"name\": \"김길동\",\n" +
                "\t\t\"age\": 16,\n" +
                "\t\t\"학교\": \"길동중\"\n" +
                "\t}";

    }
    //------------------------실행 테스트-----------------------

    //----------------------Project API------------------------
    //API1 : 사용자가 생성한 그룹 리스트 가져오기
    public List<Group> getGroupList(Integer userId) {
        return groupRepository.getGroupList(userId);
    }

    //API2 + API3 : 그룹 생성 & 초대코드 return
    public String createGroup(Integer uid, String groupTitle, String groupDetail) {

        String inviteCode = RandomStringUtils.randomAlphabetic(10); //랜덤 문자열 생성 : 초대코드
        groupRepository.createGroup(inviteCode, uid, groupTitle, groupDetail);   //그룹 생성
        return groupRepository.getInviteCode(); //초대코드 반환
    }

    //API4 : 출석 코드 생성 : GROUP Table update
    public void putAttendanceCode(Integer gid, LocalDateTime acceptStartTime, LocalDateTime acceptEndTime) {
       String attendanceCode = RandomStringUtils.randomAlphabetic(5); //랜덤 문자열 생성 : 출석코드
        groupRepository.putAttendanceCode(attendanceCode, gid);   //출석 코드 update
        groupRepository.updateCodeState(gid);
        groupRepository.insertCode(gid, attendanceCode, acceptStartTime, acceptEndTime);

    }

    //API5 : 출석 코드 조회
    public String getAttendanceCode(Integer gid) {
        return groupRepository.getAttendanceCode(gid);
    }

    //API6 : 자신이 참여한 그룹 리스트 조회
    public List<API6Response> getJoinedGroupList(Integer userId) {

        var objLists = groupRepository.getJoinedGroupList(userId);

        ArrayList<API6Response> result = new ArrayList<API6Response>();

        for(int i=0; i < objLists.size(); i++){
            var list = objLists.get(i);
            result.add(new API6Response(
                    (Integer)list[0], (Integer)list[1], (String)list[2],
                    (String)list[3], (String)list[4], list[5].toString(),
                    (Integer)list[6], (Integer)list[7], (String)list[8]
            ));
        }

        return result;
    }

    //API7 : 접속한 그룹 정보 조회
    public Group getGroupInfo(Integer gid) {
        return groupRepository.getGroupInfo(gid);
    }


    //API8 : 사용자의 출석 상태 Insert
    public void insertUserAttendance(String guid, LocalDateTime enterTime, String attendanceCode) {
        groupRepository.insertUserAttendance(guid, enterTime, attendanceCode);
        groupRepository.updateHid(guid);
    }

    public void updateUserAttendance(String guid, LocalDateTime exitTime) {
        groupRepository.updateUserAttendance(guid, exitTime);
    }

    public void insertGroupUser(Integer uid, String userCode) {
        groupRepository.insertGroupUser(uid, userCode);
    }

    //----------------------Project API------------------------
}
