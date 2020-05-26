package com.fsd.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsd.domain.User;
import com.fsd.repository.UserRepository;
import com.fsd.service.UserService;
import com.fsd.util.ResponseCode;
import com.fsd.util.ResponseResult;
import com.fsd.vo.Password;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
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
	public User save(User user) {
		// TODO Auto-generated method stub
		User _user = this.userRepository.save(user);
		return _user;
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

	@Override
	public ResponseResult updateConfirmById(Integer id) {
		// TODO Auto-generated method stub
		try {
			this.userRepository.updateConfirmById(id);
            return ResponseResult.build(ResponseCode.SUCCESS, "SUCCESS!");
        } catch (Exception e) {
        	return ResponseResult.build(ResponseCode.ERROR_ACCESS_DB, "DB ERROR!");
        }
	}

	@Override
	public ResponseResult updateProfileById(User user) {
		// TODO Auto-generated method stub
		try {
			User oldUser = this.userRepository.findById(user.getId());
			oldUser.setUsername(user.getUsername());
			oldUser.setEmail(user.getEmail());
			Date date = new Date(); 
			oldUser.setUpdatets(new Timestamp(date.getTime()));
            this.userRepository.save(oldUser);
            return ResponseResult.build(ResponseCode.SUCCESS, "SUCCESS!");
        } catch (Exception e) {
        	return ResponseResult.build(ResponseCode.ERROR_ACCESS_DB, "DB ERROR!");
        }
	}

	@Override
	public ResponseResult updatePasswordById(Password password) {
		// TODO Auto-generated method stub
		try {
			User oldUser = this.userRepository.findById(password.getId());
			if(bCryptPasswordEncoder.matches(password.getOldpassword(),oldUser.getPassword() )) {
				oldUser.setPassword(bCryptPasswordEncoder.encode(password.getNewpassword()));
				Date date = new Date(); 
				oldUser.setUpdatets(new Timestamp(date.getTime()));
	            this.userRepository.save(oldUser);
	            return ResponseResult.build(ResponseCode.SUCCESS, "SUCCESS!");
			}else {
				return ResponseResult.build(ResponseCode.ERROR_PASSWORD, "Password Incorrect!");
			}
			
        } catch (Exception e) {
        	return ResponseResult.build(ResponseCode.ERROR_ACCESS_DB, "DB ERROR!");
        }
	}
	
	
}
