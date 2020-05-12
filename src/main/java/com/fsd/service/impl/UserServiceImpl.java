package com.fsd.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsd.domain.User;
import com.fsd.repository.UserRepository;
import com.fsd.service.UserService;
import com.fsd.util.ResponseCode;
import com.fsd.util.ResponseResult;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public ResponseResult findAll() {
		try {
			List<User> list = this.userRepository.findAll();
            return ResponseResult.build(ResponseCode.SUCCESS, "SUCCESS!", list);
        } catch (Exception e) {
            return ResponseResult.build(ResponseCode.ERROR_ACCESS_DB, "DB ERROR!");
        }  
	}

	@Override
	public ResponseResult findById(Integer id) {
		// TODO Auto-generated method stub
		try {
			User user = this.userRepository.findById(id);
            return ResponseResult.build(ResponseCode.SUCCESS, "SUCCESS!", user);
        } catch (Exception e) {
            return ResponseResult.build(ResponseCode.ERROR_ACCESS_DB, "DB ERROR!");
        } 
	}

	@Override
	public ResponseResult save(User user) {
		// TODO Auto-generated method stub
		try {
			User _user = this.userRepository.save(user);
            return ResponseResult.build(ResponseCode.SUCCESS, "SUCCESS!", _user);
        } catch (Exception e) {
            return ResponseResult.build(ResponseCode.ERROR_ACCESS_DB, "DB ERROR!");
        }
	}

	@Override
	public ResponseResult updateUserById(User user) {
		// TODO Auto-generated method stub
		try {
			User oldUser = this.userRepository.findById(user.getId());
			oldUser.setUsername(user.getUsername());
			oldUser.setUsertype(user.getUsertype());
			oldUser.setEmail(user.getEmail());
			oldUser.setMobilenum(user.getMobilenum());
			oldUser.setPassword(user.getPassword());
			oldUser.setConfirmed(user.getConfirmed());
			Date date = new Date(); 
			oldUser.setUpdatets(new Timestamp(date.getTime()));
            this.userRepository.save(oldUser);
            return ResponseResult.build(ResponseCode.SUCCESS, "SUCCESS!");
        } catch (Exception e) {
        	return ResponseResult.build(ResponseCode.ERROR_ACCESS_DB, "DB ERROR!");
        }
	}


}
