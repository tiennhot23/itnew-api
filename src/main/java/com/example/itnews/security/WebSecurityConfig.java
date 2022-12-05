package com.example.itnews.security;

import com.example.itnews.security.exceptions.CustomAccessDeniedHandler;
import com.example.itnews.security.exceptions.CustomAuthenticationEntryPoint;
import com.example.itnews.security.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@EnableWebSecurity
//@EnableGlobalMethodSecurity(
//        securedEnabled = true,
//        jsr250Enabled = true,
//        prePostEnabled = true
//)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SecureUserService secureUserService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(secureUserService) // Cung cáp userservice cho spring security
                .passwordEncoder(passwordEncoder()); // cung cấp password encoder
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v2/account/login",
                        "/api/v2/account/forgot/password",
                        "/api/v2/account/{idAccount}/confirm",
                        "/api/v2/account/")
                .permitAll() // Cho phép tất cả mọi người truy cập vào địa chỉ này
                .antMatchers(HttpMethod.GET, "/api/v2/account/all",
                        "/api/v2/account/search",
                        "/api/v2/account/{id}",
                        "/api/v2/account/{id}/role",
                        "/api/v2/account/{id}/posts",
                        "/api/v2/account/{id}/bookmarks",
                        "/api/v2/account/{id}/follow_tag",
                        "/api/v2/account/{id}/follower",
                        "/api/v2/account/{id}/following",
                        "/api/v2/account/{id}/mark",
                        "/api/v2/account/{id}/view",
                        "/api/v2/account/{id}/status/{id_user}",
                        "/api/v2/tag/all",
                        "/api/v2/tag/{id_account}/all",
                        "/api/v2/tag/search",
                        "/api/v2/tag/{id}",
                        "/api/v2/tag/{id}/post",
                        "/api/v2/post/search",
                        "/api/v2/post/newest",
                        "/api/v2/post/newest/all",
                        "/api/v2/post/trending",
                        "/api/v2/post/{id}",
                        "/api/v2/post/{id}/voteup",
                        "/api/v2/post/{id}/votedown",
                        "/api/v2/post/{id}/vote",
                        "/api/v2/post/{id_post}/comment",
                        "/api/v2/post/{id_post}/comment/main",
                        "/api/v2/post/{id_post}/comment/reply/{id_cmt_parent}")
                .permitAll()
                .anyRequest() // Tất cả các request khác đều cần phải xác thực mới được truy cập
                .authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler());

        // Thêm một lớp Filter kiểm tra jwt
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("https://example.com"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
}

