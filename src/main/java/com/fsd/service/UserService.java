package com.fsd.service;

import com.fsd.domain.User;
import com.fsd.util.ResponseResult;
import com.fsd.vo.Password;

public interface UserService {
	
	ResponseResult findAll();
	ResponseResult findById(Integer id);
	User save(User user);
    ResponseResult updateUserById(User user);
    ResponseResult updateConfirmById(Integer id);
    ResponseResult updateProfileById(User user);
    ResponseResult updatePasswordById(Password password);

}
