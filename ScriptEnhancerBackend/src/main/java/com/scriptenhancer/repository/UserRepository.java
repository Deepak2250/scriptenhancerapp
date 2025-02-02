package com.scriptenhancer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.scriptenhancer.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);
    
    @Modifying // use when modifying of delete should and void
    @Transactional
    @Query("update User u set u.profile = null where u.email = :email")
    int deleteUserProfileImage(@Param("email") String email);
}
