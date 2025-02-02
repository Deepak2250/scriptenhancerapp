package com.scriptenhancer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scriptenhancer.entities.InputAndOutput;
import com.scriptenhancer.entities.User;

@Repository
public interface InputAndOutputRepository extends JpaRepository<InputAndOutput, Long> {

	//SELECT io.* FROM InputAndOutput io JOIN User u ON io.user_id = u.id WHERE u.id = :userId;

	@Query("SELECT io FROM InputAndOutput io JOIN io.user u WHERE u = :user") 
	List<InputAndOutput> findAllByUser(@Param("user") User user);
	}