package com.code.Repository;

import com.code.Entity.group_tb;
import com.code.Entity.history_tb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HistoryRepository extends JpaRepository<history_tb, Integer> {

    @Query(value = "SELECT * FROM attender.history_tb h where h.hid = :hid", nativeQuery = true)
    history_tb getHistoryState(Integer hid);




}
