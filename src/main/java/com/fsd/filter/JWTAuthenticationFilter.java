package com.fsd.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.support.RequestContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsd.domain.JwtUser;
import com.fsd.util.JwtTokenUtils;
import com.fsd.util.ResponseCode;
import com.fsd.vo.LoginUser;
import com.fsd.vo.UserInfo;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		super.setFilterProcessesUrl("/auth/login");
	}
	
	@Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

    	//get login info from inputstream
        try {
            LoginUser loginUser = new ObjectMapper().readValue(request.getInputStream(), LoginUser.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    

    /**
     * If authentication is success,return a generated token 
     */
	@Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

		try {
			JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
	        boolean isRemember = false;
	        String role = "";
	        Collection<? extends GrantedAuthority> authorities = jwtUser.getAuthorities();
	        for (GrantedAuthority authority : authorities){
	            role = authority.getAuthority();
	        }
	        System.out.println("jwtUser:" + jwtUser.toString());
	        String token = JwtTokenUtils.createToken(jwtUser.getUsername(),role, isRemember);
	        //add `Bearer token` before token        
		    response.setContentType("application/json; charset=utf-8");
		    response.setHeader("token", JwtTokenUtils.TOKEN_PREFIX + token);
		    
//		    response.setHeader("Access-Control-Allow-Origin", "*");//* or origin as u prefer
//	        response.setHeader("Access-Control-Allow-Credentials", "true");
//	        response.setHeader("Access-Control-Allow-Methods", "PUT, POST, GET, OPTIONS, DELETE");
//	        response.setHeader("Access-Control-Max-Age", "3600");
//	        response.setHeader("Access-Control-Allow-Headers", "*");
//	        response.setHeader("Access-Control-Expose-Headers", "*");
		    PrintWriter writer = response.getWriter();
	        JSONObject o = new JSONObject();
			o.put("status", ResponseCode.SUCCESS);
			o.put("msg", "Success");
			JSONObject userinfo = new JSONObject();
			//Map map=new HashMap();
			userinfo.put("userid", jwtUser.getId());
			userinfo.put("username", jwtUser.getUsername());
			userinfo.put("role", role);
//			UserInfo userinfo = new UserInfo();
//			userinfo.setUserid(jwtUser.getId());
//			userinfo.setUsername(jwtUser.getUsername());
//			userinfo.setRole(role);
			o.put("userinfo", userinfo);
		    writer.write(o.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
    }

	/**
	 * If authentication is failed
	 */
	@Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		try {
			PrintWriter writer = response.getWriter();
	        JSONObject o = new JSONObject();        
			o.put("status", ResponseCode.BAD_CREDENTIALS);
			o.put("msg", "Bad credentials");
			writer.write(o.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
