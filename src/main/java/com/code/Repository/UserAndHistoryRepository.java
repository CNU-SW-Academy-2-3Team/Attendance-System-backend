package com.code.Repository;

import com.code.Entity.group_tb;
import com.code.Entity.user_and_history_tb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserAndHistoryRepository extends JpaRepository<user_and_history_tb, Integer> {

    @Query(value = "SELECT * FROM attender.user_and_history_tb uh WHERE uh.guid = (SELECT gu.guid FROM attender.group_and_user_tb gu WHERE (gu.gid = :gid AND gu.uid = :uid))", nativeQuery = true)
    user_and_history_tb getAttendanceState(Integer gid, Integer uid);


    @Query(value = "SELECT uh.hid FROM attender.user_and_history_tb uh WHERE uh.guid = :guid", nativeQuery = true)
    Integer getGroupMemberNowState(Integer guid);

}
