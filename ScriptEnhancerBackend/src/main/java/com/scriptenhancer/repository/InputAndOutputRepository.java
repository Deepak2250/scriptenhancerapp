package com.scriptenhancer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scriptenhancer.entities.InputAndOutput;

@Repository
public interface InputAndOutputRepository extends JpaRepository<InputAndOutput, Long> {

}