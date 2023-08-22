package com.grupob.ServiMarket.repository;

import com.grupob.ServiMarket.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    public UserEntity findByEmail(@Param("email") String email);
    @Query("SELECT u from UserEntity u WHERE u.name LIKE CONCAT('%',:query,'%')")
    List<UserEntity> searchUsername(String query);

    boolean existsByEmail(String email);

    //User findUserByEmail (String email);

    //List<UserEntity> findUserByActiveFalse();

    //List<UserEntity> findUserByActiveTrue () ;
}
