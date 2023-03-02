package com.code.Repository;

import com.code.Entity.API6Response;
import com.code.Entity.Group;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Transactional
@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {


    //API1 : 사용자가 생성한 그룹 리스트 가져오기
    @Query(value = "SELECT * FROM attendance_web_db.group g where g.master_uid = :uid", nativeQuery = true)
    public List<Group> getGroupList(@Param("uid") Integer uid);



    //API2 + API3 : 그룹 생성 & 초대코드 return
    @Modifying
    @Query(value = "INSERT INTO attendance_web_db.group " +
            "(`invite_code`, `group_title`, `group_detail`, `create_date`, `master_uid`, `head_count`) " +
            "VALUES (:invite_code, :groupTitle, :groupDetail, now(), :uid, 1)", nativeQuery = true)
    void createGroup(String invite_code, Integer uid, String groupTitle, String groupDetail);
    @Query(value = "SELECT g.invite_code FROM attendance_web_db.group g WHERE g.gid = (SELECT LAST_INSERT_ID())", nativeQuery = true)
    String getInviteCode();



    //API4 : 출석 코드 생성 : GROUP Table update
    @Modifying
    @Query(value = "UPDATE `attendance_web_db`.`group` SET `attendance_code` = :invite_code WHERE (`gid` = :gid)", nativeQuery = true)
    void putAttendanceCode(String invite_code, Integer gid);

    @Modifying
    @Query(value = "UPDATE `attendance_web_db`.`code` SET `state` = 'd' WHERE (`gid` = :gid)", nativeQuery = true)
    void updateCodeState(Integer gid);

    @Modifying
    @Query(value = "INSERT INTO `attendance_web_db`.`code` " +
            "(gid, attendanceCode, acceptStartTime, acceptEndtime, state) " +
            "VALUES (:gid, :attendance_code, :acceptStartTime, :acceptEndTime, 'a')", nativeQuery = true)
    void insertCode(Integer gid, String attendance_code, LocalDateTime acceptStartTime, LocalDateTime acceptEndTime);


    //API5 : 출석 코드 조회
    @Query(value = "SELECT g.attendance_code FROM attendance_web_db.group g where g.gid = :gid", nativeQuery = true)
    String getAttendanceCode(Integer gid);


    //API6 : 자신이 참여한 그룹 리스트 조회
    @Query(value = "SELECT gu.guid, g.*" +
                    "FROM attendance_web_db.group g " +
                    "INNER JOIN attendance_web_db.group_user gu " +
                    "ON g.gid = gu.gid " +
                    "WHERE gu.uid = :userId ", nativeQuery = true)
    List<Object[]> getJoinedGroupList(Integer userId);

    //API7 : 접속한 그룹 정보 조회
    @Query(value = "SELECT * FROM attendance_web_db.group g where g.gid = :gid", nativeQuery = true)
    Group getGroupInfo(Integer gid);



    //API8 : 사용자의 출석 상태 Insert
    @Modifying
    @Query(value = "INSERT INTO attendance_web_db.history " +
            "(`guid`, `enter_time`, `generate_time`, `attendance_code`) " +
            "VALUES (:guid, :enterTime, now(), :attendanceCode)", nativeQuery = true)
    void insertUserAttendance(String guid, LocalDateTime enterTime, String attendanceCode);
    @Modifying
    @Query(value = "UPDATE `attendance_web_db`.`group_user` SET `hid` = (SELECT LAST_INSERT_ID()) WHERE (`guid` = :guid)", nativeQuery = true)
    void updateHid(String guid);

    //API9 : 사용자의 출석 상태 Update
    @Modifying
    @Query(value = "UPDATE `attendance_web_db`.`history` SET `exit_time` = :exitTime, `attendance_state` = 'P' WHERE `hid` = (select hid from attendance_web_db.group_user where guid = :guid )", nativeQuery = true)
    void updateUserAttendance(String guid, LocalDateTime exitTime);



    //API10 : 그룹 참가
    @Modifying
    @Query(value = "INSERT INTO attendance_web_db.group_user " +
            "(`uid`, `gid`, `present_state`) " +
            "VALUES (:uid, (select gid from attendance_web_db.group g where g.invite_code = :userCode), 'Y')", nativeQuery = true)
    void insertGroupUser(Integer uid, String userCode);

}
