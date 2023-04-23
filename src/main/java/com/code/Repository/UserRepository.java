package com.code.Repository;

import com.code.Entity.user_tb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<user_tb, Integer> {

    //------------------------------Native Query : 로그인-----------------------------
    @Query(value = "SELECT u.uid FROM attender.user_tb u WHERE u.email_address = :email and u.password = :password", nativeQuery = true)
    Integer checkSignInValidation(String email, String password);


    @Query(value = "SELECT * FROM attender.user_tb u WHERE u.uid = :uid", nativeQuery = true)
    user_tb getUserInfo(Integer uid);

}
