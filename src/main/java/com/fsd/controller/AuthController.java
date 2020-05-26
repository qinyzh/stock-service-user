package com.fsd.controller;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fsd.domain.User;
import com.fsd.service.MailService;
import com.fsd.service.UserService;
import com.fsd.util.ResponseCode;
import com.fsd.util.ResponseResult;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Value("${spring.mail.confirmlink}")
    private String confirmlink;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@PostMapping("/register")
    public ResponseResult registerUser(@RequestBody User newuser){
		try {
			User user = new User();
	        user.setUsername(newuser.getUsername());
	        user.setEmail(newuser.getEmail());
	        //encode the password
	        user.setPassword(bCryptPasswordEncoder.encode(newuser.getPassword()));
	        user.setUsertype("ROLE_USER");
	        user.setConfirmed("0");//0:unconfirmed;1:confirmed
	        user.setUpdatets(new Timestamp(System.currentTimeMillis()));
	        //save new user
	        User usersaved = userService.save(user);
	        
	        //send mail
	        String content = "<h1>Confirm your email address to get started on StockMarket</h1>"
	        		+ "<p>Please click <a href='"+ confirmlink + usersaved.getId() + "'>here</a> to confirm your email address to get started on StockMarket</p>";
	        System.out.println(content);
	        String subject = "Confirm your email address on StockMarket";		
	        mailService.sendHtmlMail(newuser.getEmail(), subject, content);
	        String returnMsg = "An confirm email has been sent to your email: "+newuser.getEmail()+" .Please check it out!";
            return ResponseResult.build(ResponseCode.SUCCESS, returnMsg, usersaved);
        } catch (Exception e) {
            return ResponseResult.build(ResponseCode.ERROR_ACCESS_DB, "DB ERROR!");
        }

    }
	
	@GetMapping("/confirm/{id}")
	public ResponseResult confirmUser(@PathVariable Integer id) {
		return userService.updateConfirmById(id);	
	}
	

}
