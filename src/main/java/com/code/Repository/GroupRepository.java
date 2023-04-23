package com.code.Repository;

import com.code.Entity.group_and_user_tb;
import com.code.Entity.group_tb;
import com.code.Entity.history_tb;
import com.code.Entity.user_and_history_tb;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Transactional
@Repository
public interface GroupRepository extends JpaRepository<group_tb, Integer> {


    //------------------------------API1 : 사용자가 생성한 그룹 리스트 가져오기------------------------------
    @Query(value = "SELECT * FROM attender.group_tb g where g.leader_uid = :uid", nativeQuery = true)
    List<group_tb> getGroupList(@Param("uid") Integer uid);

    //---------------------------------API2 : 그룹 생성 & 초대코드 return ---------------------------------
    @Modifying
    @Query(value = "INSERT INTO attender.group_tb " +
            "(`group_title`, `group_detail`, `leader_uid`, `invite_code`, `availability`, `create_date_time`) " +
            "VALUES (:groupTitle, :groupDetail, :uid, :invite_code, 'Y', now())", nativeQuery = true)
    void createGroup(String invite_code, Integer uid, String groupTitle, String groupDetail);
    @Query(value = "SELECT g.invite_code FROM attender.group_tb g WHERE g.gid = (SELECT LAST_INSERT_ID())", nativeQuery = true)
    String getInviteCode();

    //-----------------------------API3 : 접속한 그룹 정보 조회 & 출석 정보 포함-----------------------------
    @Query(value = "SELECT * FROM attender.group_tb g where g.gid = :gid", nativeQuery = true)
    group_tb getGroupInfo(Integer gid);

//    @Query(value = "SELECT * FROM attender.user_and_history_tb uh WHERE uh.guid = (SELECT gu.guid FROM attender.group_and_user_tb gu WHERE (gu.gid = :gid AND gu.uid = :uid))", nativeQuery = true)
//    Object[] getAttendanceState(Integer gid, Integer uid);

//    @Query(value = "SELECT * FROM attender.history_tb h where h.hid = :hid", nativeQuery = true)
//    history_tb getHistoryState(Integer hid);

    //-----------------------------API4 : 출석 코드 생성-----------------------------
    @Modifying
    @Query(value = "INSERT INTO attender.code_tb " +
            "(attendance_code, accept_start_time, accept_end_time, create_date_time) " +
            "VALUES (:attendanceCode, :acceptStartTime, :acceptEndTime, now())", nativeQuery = true)
    void insertAttendanceCode(String attendanceCode, LocalDateTime acceptStartTime, LocalDateTime acceptEndTime);

    @Modifying
    @Transactional
    @Query(value = "UPDATE attender.group_tb SET cid = (SELECT LAST_INSERT_ID()) WHERE gid = :gid", nativeQuery = true)
    void putGroupCid(Integer gid);

    //-----------------------------API5 : 출석 코드 조회-----------------------------
    @Query(value = "SELECT c.attendance_code FROM attender.code_tb c WHERE c.cid = (SELECT g.cid FROM attender.group_tb g where g.gid = :gid)", nativeQuery = true)
    String getAttendanceCode(Integer gid);

    //-----------------------------API6 : 자신이 참여한 그룹 리스트 조회-----------------------------
//    @Query(value = "SELECT * FROM attender.group_and_user_tb g where g.uid = :uid", nativeQuery = true)
//    List<Object[]> getJoinedGroupList(@Param("uid") Integer uid);
    @Query(value = "SELECT * FROM attender.group_tb g where g.gid = :gid", nativeQuery = true)
    group_tb getJoinedGroupInfo(Integer gid);

    //--------------------API7 : 메인페이지 - 전체 회원수, 그룹수, 오늘 출석한 사람 수--------------------
    @Query(value = "SELECT count(u.uid) FROM attender.user_tb u", nativeQuery = true)
    Integer getAllMemberConut();
    @Query(value = "SELECT count(g.gid) FROM attender.group_tb g", nativeQuery = true)
    Integer getAllGroupCount();
    @Query(value = "SELECT count(uh.uhid) FROM attender.user_and_history_tb uh", nativeQuery = true)
    Integer getTodayAttendCount();

    //------------------------------API8 : 사용자의 출석 상태 Insert------------------------------
    @Query(value = "SELECT g.cid FROM attender.group_tb g WHERE g.gid = (SELECT gu.gid FROM attender.group_and_user_tb gu WHERE gu.guid = :guid)", nativeQuery = true)
    Integer getGroupCurrentCid(String guid);

    @Query(value = "SELECT c.cid FROM attender.code_tb c WHERE c.attendance_code = :attendanceCode", nativeQuery = true)
    Integer getFoundCid(String attendanceCode);


    @Query(value = "SELECT c.accept_start_time FROM attender.code_tb c WHERE c.cid = :cid", nativeQuery = true)
    LocalDateTime getAcceptStartTime(Integer cid);

    @Query(value = "SELECT c.accept_end_time FROM attender.code_tb c WHERE c.cid = :cid", nativeQuery = true)
    LocalDateTime getAcceptEndTime(Integer cid);

    @Modifying
    @Query(value = "INSERT INTO attender.history_tb " +
            "(guid, enter_time, cid, create_date_time) " +
            "VALUES (:guid, :enterTime, :groupCid, now())", nativeQuery = true)
    void insertUserAttendanceToHistory(String guid, LocalDateTime enterTime, Integer groupCid);

    @Modifying
    @Query(value = "INSERT INTO attender.user_and_history_tb " +
            "(hid, guid) " +
            "VALUES ((SELECT LAST_INSERT_ID()), :guid)", nativeQuery = true)
    void insertUserAttendanceToUAH(String guid);






    //------------------------------API9 : 사용자의 출석 상태 Update------------------------------
    @Modifying
    @Transactional
    @Query(value = "UPDATE attender.history_tb SET exit_time = :exitTime, attendance_state = 'Y' WHERE hid = (SELECT hid FROM attender.user_and_history_tb WHERE guid = :guid )", nativeQuery = true)
    void updateUserAttendance(String guid, LocalDateTime exitTime);


    //------------------------------API10 : 그룹 참가------------------------------

    @Query(value = "SELECT g.gid FROM attender.group_tb g WHERE g.invite_code = :userCode", nativeQuery = true)
    Integer getGroupInviteCode(String userCode);

    @Query(value = "SELECT gu.guid FROM attender.group_and_user_tb gu WHERE gu.gid = :gid and gu.uid = :uid", nativeQuery = true)
    Integer getIsUserInGroup(Integer uid, Integer gid);

    @Modifying
    @Query(value = "INSERT INTO attender.group_and_user_tb (uid, gid) VALUES (:uid, :gid)", nativeQuery = true)
    void insertGroupUser(Integer uid, Integer gid);


    //------------------------------API11 : 그룹의 회원수-----------------------------
    @Query(value = "SELECT count(g.gid) FROM attender.group_and_user_tb g GROUP BY g.gid HAVING g.gid = :gid", nativeQuery = true)
    Integer getGroupUserCount(Integer gid);









    @Transactional
    @Query(value = "COMMIT;", nativeQuery = true)

    void commit();
    @Transactional
    @Query(value = "start transaction;", nativeQuery = true)

    void transactionStart();





}
