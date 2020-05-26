package com.fsd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fsd.domain.User;

public interface UserRepository extends JpaRepository<User,Long>{
	
	User findById(Integer id);
	
	User findByUsername(String username);
	
	@Query(value="select * from user_info where confirmed='1' and user_name=:username",nativeQuery = true)
	User findWithConfirmedByUsername(@Param("username")String username);
	
	@Modifying
	@Query(value="update user_info m set m.confirmed=1 where  m.id=:id",nativeQuery = true)
	void updateConfirmById(@Param("id")Integer id);
}
