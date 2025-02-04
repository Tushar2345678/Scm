package com.scm.scm20.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.scm20.entities.Providers;
import com.scm.scm20.entities.User;
import com.scm.scm20.helpers.AppConstants;
import com.scm.scm20.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthentcationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger=LoggerFactory.getLogger(OAuthAuthentcationSuccessHandler.class);     

    @Autowired
    private UserRepo userRepo;


    @SuppressWarnings("null")
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, 
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
                
            logger.info("OAuthAuthentcationSuccessHandler");
            
            //identify the provider

            var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

            String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

            logger.info("Authorized Client Registration ID: {}", authorizedClientRegistrationId);

            var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

            oauthUser.getAttributes().forEach((key,value)->{
                logger.info(key+" : "+value);
            });


            User user = new User();
            user.setUserId(UUID.randomUUID().toString());
            user.setRoleList(List.of(AppConstants.ROLE_USER));
            user.setEmailVerified(true);
            user.setEnabled(true);
            user.setPassword("dummy");

            if(authorizedClientRegistrationId.equalsIgnoreCase("google")){

            //google
            //google attributes

            user.setEmail(oauthUser.getAttribute("email").toString());
            user.setProfilePic(oauthUser.getAttribute("picture").toString());
            user.setName(oauthUser.getAttribute("name").toString());
            user.setProviderUserId(oauthUser.getName());
            user.setProvider(Providers.GOOGLE);
            user.setAbout("This account is created using Google");

            }
            else if(authorizedClientRegistrationId.equalsIgnoreCase("github")){

            //GitHub
            //GitHub attributes

            String email = oauthUser.getAttribute("email") !=null ? oauthUser.getAttribute("email").toString() 
                 : oauthUser.getAttribute("login").toString() +"@gmail.com";
            String picture = oauthUser.getAttribute("avatar_url").toString();
            String name = oauthUser.getAttribute("login").toString();
            String providerUserId = oauthUser.getName();

            user.setEmail(email);
            user.setProfilePic(picture);
            user.setName(name);
            user.setProviderUserId(providerUserId);
            user.setProvider(Providers.GITHUB);

            user.setAbout("This account is created using GitHub");
                
            }
            else if(authorizedClientRegistrationId.equalsIgnoreCase("Linkedin")){
            
            //LinkedIn
            //LinkedIn attributes

            }
            else if(authorizedClientRegistrationId.equalsIgnoreCase("linked")) {
                
            }
            else{ 
                logger.info("OAuthAuthenticationSuccessHandler:unknown provider");
            }
             
                User user2 = userRepo.findByEmail(user.getEmail()).orElse(null);
                if (user2 == null){
                    userRepo.save(user);
                    System.out.println("user saved:" + user.getEmail());
                }
                new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    

    }
}

