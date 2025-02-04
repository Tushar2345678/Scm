package com.scm.scm20.config;

import com.scm.scm20.services.impl.SecurityCustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDetailService userDetailService;

    @Autowired
    private OAuthAuthentcationSuccessHandler handler;

    @Autowired
    private AuthFailureHandller authFailureHandller;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeHttpRequests(authorize -> {
                //authorize.requestMatchers("/home", "/register", "/login", "/do-register").permitAll();
                authorize.requestMatchers("/user/**").authenticated();
                authorize.anyRequest().permitAll();
            });
            httpSecurity.formLogin(formLogin -> {
                formLogin.loginPage("/login");
                formLogin.loginProcessingUrl("/authenticate");
                formLogin.successForwardUrl("/user/dashboard");
                formLogin.usernameParameter("email");
                formLogin.passwordParameter("password");

                formLogin.failureHandler(authFailureHandller); 
            });

            httpSecurity.csrf(AbstractHttpConfigurer::disable);
            httpSecurity.logout(logoutForm -> {
                logoutForm.logoutUrl("/do-logout");
                logoutForm.logoutSuccessUrl("/login?logout=true");
                 
                
            });
            httpSecurity.oauth2Login(oauth -> {
                oauth.loginPage("/login");
                oauth.successHandler(handler);
            });



        return httpSecurity.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}