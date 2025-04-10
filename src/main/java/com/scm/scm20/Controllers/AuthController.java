package com.scm.scm20.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.scm20.entities.User;
import com.scm.scm20.helpers.Messages;
import com.scm.scm20.helpers.MessageType;
import com.scm.scm20.repositories.UserRepo;
//import com.scm.scm20.services.UserService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepo userRepo;

    //verify email
    @GetMapping("/verify-email")
    public String verifyEmail(
        @RequestParam("token") String token, HttpSession session) {
       
       User user = userRepo.findByEmailToken(token).orElse(null);

       System.out.println("verify Email");

        if (user != null) {

            if(user.getEmailToken().equals(token)) {
                user.setEmailVerified(true);
                user.setEnabled(true);
                userRepo.save(user);
                session.setAttribute("message", Messages.builder()
                    .type(MessageType.green)
                    .content("Email is verified : Account is activated")
                    .build());
                return "success_page";  
            } 

            session.setAttribute("message", Messages.builder()
                .type(MessageType.red)
                .content("Email is not verified : Invalid Token")
                .build());
            return "error_page";
        }
        
        session.setAttribute("message", Messages.builder()
            .type(MessageType.red)
            .content("Email is not verified : Invalid Token")
            .build());

        return "error_page";
    }
    
}
