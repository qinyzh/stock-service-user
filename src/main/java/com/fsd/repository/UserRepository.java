package com.fsd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fsd.domain.User;

public interface UserRepository extends JpaRepository<User,Long>{
	
	User findById(Integer id);
}
