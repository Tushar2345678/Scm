package com.scm.scm20.Controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;


import com.scm.scm20.entities.Message;
import com.scm.scm20.entities.User;
import com.scm.scm20.services.ContactService;
import com.scm.scm20.services.MessageService;
import com.scm.scm20.services.UserService;

@Controller
@RequestMapping("/user")
public class MessagePageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @GetMapping("/direct_message")
    public String showMessageForm(@RequestParam(required = false) String contactId,
                                  Model model,
                                  Principal principal) {
    
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User sender;
    
        if (authentication.getPrincipal() instanceof OAuth2User oauthUser) {
            String email = (String) oauthUser.getAttributes().get("email");
    
            if (email != null) {
                sender = userService.getUserByEmail(email);
            } else {
                // Fallback to GitHub ID
                String githubId = String.valueOf(oauthUser.getAttributes().get("id"));
                sender = userService.getUserByProviderUserId(githubId);
            
            sender = userService.getUserByEmail(email);
            }
            
            if (sender == null) {
                throw new RuntimeException("Authenticated user not found in DB for email: " + email);
            }
    
        } else {
            throw new RuntimeException("OAuth2 authentication expected, but got: " + authentication.getPrincipal());
        }
    
        model.addAttribute("loggedInUser", sender);
    
        // Load user's contacts
        List<User> contacts = contactService.getContactUsersOfUser(sender.getUserId());
        model.addAttribute("contacts", contacts);
    
        if (contactId != null) {
            User selectedContact = userService.getUserByUId(contactId);
            model.addAttribute("selectedContact", selectedContact);
    
            List<Message> messages = messageService.getConversation(sender.getUserId(), contactId);
            model.addAttribute("messages", messages);
        } else {
            model.addAttribute("messages", List.of());
            model.addAttribute("selectedContact", new User());
        }
    
        model.addAttribute("message", new Message());
        System.out.println("Contact count: " + contacts.size());
        return "user/direct_message";
    }
    

    @PostMapping("/messages/send-form")
    public String sendMessageForm(@ModelAttribute Message message) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User sender;

    if (authentication.getPrincipal() instanceof OAuth2User oauthUser) {
        String email = (String) oauthUser.getAttributes().get("email");
        if (email == null) {
            throw new RuntimeException("Email not available in GitHub OAuth response.");
        }
        sender = userService.getUserByEmail(email);
    } else {
        throw new RuntimeException("OAuth2 authentication expected.");
    }

    message.setSender(sender);
    messageService.sendMessage(message);

    return "redirect:/user/direct_message?contactId=" + message.getReceiver().getUserId();
}

}