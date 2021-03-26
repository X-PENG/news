package com.peng.news.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peng.news.component.CustomizedAccessDecisionManager;
import com.peng.news.component.CustomizedSecurityMetadataSource;
import com.peng.news.model.Result;
import com.peng.news.model.vo.UserVO;
import com.peng.news.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author  PENG
 * @date  2021/3/23 14:04
 * @version 1.0
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Configuration
    @Order(1)
    public static class ManagementSecurityConfig extends WebSecurityConfigurerAdapter{
        @Autowired
        CustomizedSecurityMetadataSource customizedSecurityMetadataSource;

        @Autowired
        CustomizedAccessDecisionManager customizedAccessDecisionManager;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //拦截所有访问后台管理资源的请求
            http.antMatcher("/management/**").authorizeRequests()
                    .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                        @Override
                        public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                            object.setSecurityMetadataSource(customizedSecurityMetadataSource);
                            object.setAccessDecisionManager(customizedAccessDecisionManager);
                            return object;
                        }
                    })
                    .anyRequest().authenticated()
                    .and()
                    //必须要禁用防止csrf攻击的措施，否则，所有POST、PUT、DELETE类型的后台管理请求都不能用！（除非携带csrf参数）
                    .csrf()
                    .disable();
        }
    }

    @Configuration
    @Order(2)
    public static class OtherSecurityConfig extends WebSecurityConfigurerAdapter{
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/hello/admin").hasRole("admin")
                    .antMatchers("/hello/editor").hasRole("editor")
                    .antMatchers("/hello/deliverer").hasRole("deliverer")
                    .anyRequest()
                    .permitAll()
                    .and()
                    .formLogin()
                    .loginProcessingUrl("/doLogin")
                    .successHandler(new AuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                            resp.setContentType("application/json;charset=utf-8");
                            PrintWriter writer = resp.getWriter();
                            String msg = "登录成功！";
                            UserVO userVO = (UserVO) authentication.getPrincipal();
                            userVO.setPasswd(null);
                            writer.write(new ObjectMapper().writeValueAsString(Result.success(msg, userVO)));
                            writer.close();
                        }
                    })
                    .failureHandler(new AuthenticationFailureHandler() {
                        @Override
                        public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException exception) throws IOException, ServletException {
                            resp.setContentType("application/json;charset=utf-8");
                            PrintWriter writer = resp.getWriter();
                            String msg = "登录失败！";
                            if(exception instanceof BadCredentialsException){
                                msg = "用户名或密码错误，登录失败！";
                            }else if(exception instanceof LockedException){
                                msg = "账号被锁定，登录失败！";
                            }
                            writer.write(new ObjectMapper().writeValueAsString(Result.fail(msg)));
                            writer.close();
                        }
                    })
                    .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(new LogoutSuccessHandler() {
                        @Override
                        public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                            resp.setContentType("application/json;charset=utf-8");
                            PrintWriter writer = resp.getWriter();
                            String msg = "注销成功！";
                            Result<Object> result = Result.success(msg);
                            if(authentication == null){
                                resp.setStatus(401);
                                msg = "尚未登录，请先登录！";
                                result = Result.fail("401", msg);
                            }
                            writer.write(new ObjectMapper().writeValueAsString(result));
                            writer.close();
                        }
                    })
                    .and()
                    .csrf()
                    .disable();
        }
    }
}
