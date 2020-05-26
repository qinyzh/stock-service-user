package com.fsd.config;


import java.io.PrintWriter;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsd.exception.JWTAccessDeniedHandler;
import com.fsd.exception.JWTAuthenticationEntryPoint;
import com.fsd.filter.JWTAuthenticationFilter;
import com.fsd.filter.JWTAuthorizationFilter;
import com.fsd.util.ResponseCode;
import com.fsd.util.ResponseResult;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Resource
    ObjectMapper objectMapper;
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
			 .authorizeRequests()
			 .antMatchers("/user/**").authenticated()
			 .antMatchers("/company/**").authenticated()
			 //.antMatchers("").hasAuthority("ROLE_ADMIN")
			 .anyRequest().permitAll()
			 .and()
             .logout()
             .logoutSuccessHandler(logoutSuccessHandler())//登出处理器
             .logoutUrl("/auth/logout")//登出api
             .permitAll()
			 .and()
			 .addFilter(new JWTAuthenticationFilter(authenticationManager()))
			 .addFilter(new JWTAuthorizationFilter(authenticationManager()))
			  // 使用无状态的session，session不会存储用户信息
			 .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			 .and() //timeout handle
			 .exceptionHandling().authenticationEntryPoint(new JWTAuthenticationEntryPoint())
			 .accessDeniedHandler(new JWTAccessDeniedHandler()); 
	 }
	
//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
//        return source;
//    }
	
	private LogoutSuccessHandler logoutSuccessHandler(){
        return (request, response, authentication) -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null){
                new SecurityContextLogoutHandler().logout(request, response, auth);//清除登录认证信息
            }
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            ResponseResult ok = ResponseResult.build(ResponseCode.SUCCESS, "Success Logout!");
            out.write(objectMapper.writeValueAsString(ok));
            out.flush();
            out.close();
        };
    }
}
