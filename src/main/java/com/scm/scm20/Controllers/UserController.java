package com.scm.scm20.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.scm20.entities.User;
import com.scm.scm20.services.UserService;



@Controller
@RequestMapping("/user")
public class UserController {

   
    @SuppressWarnings("unused")
    private Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private UserService userService;

    // user dashbaord page

    @RequestMapping(value = "/dashboard")
    public String userDashboard() {
        System.out.println("User dashboard");
        return "user/dashboard";
    }

    // user profile page
   @GetMapping("/profile")
    public String userProfile(Model model, Authentication authentication) {
         String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user); 
        return "user/profile";
    }

    // user add contacts page
    /* @GetMapping("/add_contact")
    public String userAdd_contact() {
        return "user/add_contact";
    }
 */
    // user profile update

    // user delete contact

}