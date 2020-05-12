package com.fsd.service;

import com.fsd.domain.User;
import com.fsd.util.ResponseResult;

public interface UserService {
	
	ResponseResult findAll();
	ResponseResult findById(Integer id);
	ResponseResult save(User user);
    ResponseResult updateUserById(User user);	

}
