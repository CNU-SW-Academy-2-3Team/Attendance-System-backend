package com.code.Repository;

import com.code.Entity.group_and_user_tb;
import com.code.Entity.group_tb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupAndUserRepository extends JpaRepository<group_and_user_tb, Integer> {
    @Query(value = "SELECT * FROM attender.group_and_user_tb g where g.uid = :uid", nativeQuery = true)
    List<group_and_user_tb> getJoinedGroupList(@Param("uid") Integer uid);


    @Query(value = "SELECT * FROM attender.group_and_user_tb g where g.gid = :gid", nativeQuery = true)
    List<group_and_user_tb> getJoinedMemberList(@Param("gid") Integer gid);

}
