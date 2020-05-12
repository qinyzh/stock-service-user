package com.fsd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fsd.domain.User;
import com.fsd.service.UserService;
import com.fsd.util.ResponseResult;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/{id}")
    public ResponseResult queryUserById(@PathVariable Integer id){
        return userService.findById(id);
    }
	
	@PostMapping
    public ResponseResult addUser(@RequestBody User user){
        return userService.save(user);
    }
    
    @PutMapping
    public ResponseResult editUser(@RequestBody User user) {
    	return userService.updateUserById(user);
    }

}
