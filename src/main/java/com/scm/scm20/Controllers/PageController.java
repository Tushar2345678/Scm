package com.scm.scm20.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;   

import com.scm.scm20.entities.User;
import com.scm.scm20.forms.UserForm;
import com.scm.scm20.helpers.Messages;
import com.scm.scm20.helpers.MessageType;
import com.scm.scm20.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        System.out.println("Home page handler");
        return "home";
    }

    // about route

    @RequestMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("isLogin", true);
        System.out.println("About page loading");
        return "about";
    }

    // services

    @RequestMapping("/services")
    public String servicesPage() {
        System.out.println("services page loading");
        return "services";
    }

    // contact page

    @GetMapping("/contact")
    public String contact() {
        return new String("contact");
    }

    // this is showing login page
    @GetMapping("/login")
    public String login() {
        return new String("login");
    }

    // registration page
    @GetMapping("/register")
    public String register(Model model) {
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        return "register";
    }

    // processing register form
    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession session) {
    System.out.println("Processing registration");
    // fetch form data
    // UserForm
    System.out.println(userForm);

    // Validate form data
    if (rBindingResult.hasErrors()) {
        return "register";
    }
    // Check if email already exists
    if (userService.isUserExistByEmail(userForm.getEmail())) {
        Messages message = Messages.builder().content("Email already exists").type(MessageType.blue).build();
        session.setAttribute("message", message);
        return "register";
    }

    // Save user to database
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(false); 
        user.setProfilePic("C:\\Users\\TUSHAR\\OneDrive\\Desktop\\WhatsApp Image 2025-01-08 at 21.19.25.jpg");

        User savedUser = userService.saveUser(user);
        System.out.println("user saved : " + savedUser);
       
        Messages message = Messages.builder().content("Registration Successful").type(MessageType.green).build(); 
        session.setAttribute("message", message);

    return "redirect:/login";
}

}
