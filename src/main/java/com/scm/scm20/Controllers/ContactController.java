package com.scm.scm20.Controllers;


import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.scm20.entities.Contact;
import com.scm.scm20.entities.User;
import com.scm.scm20.forms.ContactForm;
import com.scm.scm20.forms.ContactSearchForm;
import com.scm.scm20.helpers.AppConstants;
import com.scm.scm20.helpers.Helper;
import com.scm.scm20.helpers.Message;
import com.scm.scm20.helpers.MessageType;
import com.scm.scm20.services.ContactService;
import com.scm.scm20.services.ImageService;
import com.scm.scm20.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(ContactController.class);
    
    @Autowired
    private ImageService imageService;
    
    @Autowired
    private ContactService contactService;  

    @Autowired
    private UserService userService;

    @RequestMapping("/add")
    public String addContactView(Model model){
        ContactForm contactForm = new ContactForm();
        contactForm.setFavorite(true);
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result, Authentication authentication, HttpSession session){

        // process form data

        // validate form
        if(result.hasErrors()){
            session.setAttribute("message", Message.builder()
            .content("Please correct the following errors")
            .type(MessageType.red)
            .build()
            );
            return "user/add_contact";
        }

        String username=Helper.getEmailOfLoggedInUser(authentication);

        //form---> contact
        User user = userService.getUserByEmail(username);

        //process the contact picture

        // image process

        // code for uploading file

        Contact contact=new Contact();
        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setInstagramLink(contactForm.getInstagramLink());
        contact.setFacebookLink(contactForm.getFacebookLink());
        contact.setTwitterLink(contactForm.getTwitterLink());
        
        if(contactForm.getContactImage() !=null && !contactForm.getContactImage().isEmpty()){
            String fileName = UUID.randomUUID().toString();
            String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
            contact.setPicture(imageUrl);
            contact.setCloudinaryImagePublicId(fileName); 
            
            
           }

        contactService.save(contact); 
        System.out.println(contactForm);

        //set the contact picture url

        //set the message to be displayed on the view
        session.setAttribute("message",  
            Message.builder()
                .content("you have successfully added a new contact")
                .type(MessageType.green)
                .build()
        );

        return "redirect:/user/contacts/add";
    }


        // view contacts
    @RequestMapping
    public String viewContacts(
    @RequestParam(value = "page",defaultValue = "0" ) int page,
    @RequestParam(value = "size",defaultValue = AppConstants.PAGE_SIZE+"") int size,
    @RequestParam(value = "sortBy",defaultValue = "name") String sortBy,
    @RequestParam(value = "direction",defaultValue = "asc") String direction,Model model, Authentication authentication){
        
        //load all the user contacts
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contact> pageContact = contactService.getByUser(user,page,size,sortBy,direction);
        
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("contactSearchForm", new ContactSearchForm());

        return "user/contacts";
    }


    // search handler

    @RequestMapping("/search")
    public String searchHandler(
        @ModelAttribute ContactSearchForm contactSearchForm, Model model,
        @RequestParam(value = "page",defaultValue = "0" ) int page,
        @RequestParam(value = "size",defaultValue = AppConstants.PAGE_SIZE+"") int size,
        @RequestParam(value = "sortBy",defaultValue = "name") String sortBy,
        @RequestParam(value = "direction",defaultValue = "asc") 
        String direction,
        Authentication authentication
     ){
           

    logger.info("field {} keyword {}" , contactSearchForm.getField(),contactSearchForm.getValue());

    var user=userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

    
    Page<Contact> pageContact=null;
    if(contactSearchForm.getField().equalsIgnoreCase("name")){
        pageContact = contactService.searchByName(contactSearchForm.getValue(),size,page,sortBy,direction,user);
    }else if(contactSearchForm.getField().equalsIgnoreCase("email")){
        pageContact = contactService.searchByEmail(contactSearchForm.getValue(),size,page,sortBy,direction,user);
    }else if(contactSearchForm.getField().equalsIgnoreCase("phone")){
        pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(),size,page,sortBy,direction,user);
    }else {
        return "redirect:/user/contacts";
    }

    logger.info("pageContact {}",pageContact);    
    
    model.addAttribute("pageContact",pageContact);
    model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
    model.addAttribute("contactSearchForm", contactSearchForm);
    
    return "user/search";
    }

    // delete contact
    @RequestMapping("/delete/{contactId}")
    public String deleteContact(
        @PathVariable("contactId") String contactId,
        HttpSession session)
    {

        contactService.delete(contactId);
        logger.info("contact deleted with id {}",contactId);

        session.setAttribute("message", 
        Message.builder()
        .content("Contact deleted successfully")
        .type(MessageType.green)
        .build()
        );

        return "redirect:/user/contacts";
    }

    // update contact form view

    @GetMapping("/view/{contactId}")
    public String updateContactFormView(
        @PathVariable("contactId") String contactId, 
        Model model)
        {

        var contact=contactService.getById(contactId);
        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavorite(contact.isFavorite());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setInstagramLink(contact.getInstagramLink());
        contactForm.setFacebookLink(contact.getFacebookLink());
        contactForm.setTwitterLink(contact.getTwitterLink());
        contactForm.setPicture(contact.getPicture());

        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);

        return "user/update_contact_view";
      }


      @RequestMapping(value = "/update/{contactId}", method = RequestMethod.POST)
      public String updateContact(@PathVariable("contactId") String contactId, 
                @Valid @ModelAttribute ContactForm contactForm,
                BindingResult bindingResult,
                Model model){

        // update the contact

        if (bindingResult.hasErrors()) {
            return "user/update_contact_view";
        }

        var con = contactService.getById(contactId);
        con.setId(contactId);
        con.setName(contactForm.getName());
        con.setEmail(contactForm.getEmail());
        con.setPhoneNumber(contactForm.getPhoneNumber());
        con.setAddress(contactForm.getAddress());
        con.setDescription(contactForm.getDescription());
        con.setFavorite(contactForm.isFavorite());
        con.setLinkedInLink(contactForm.getLinkedInLink());
        con.setInstagramLink(contactForm.getInstagramLink());
        con.setFacebookLink(contactForm.getFacebookLink());
        con.setTwitterLink(contactForm.getTwitterLink());
         

        // process image:

    if(contactForm.getContactImage()!=null && !contactForm.getContactImage().isEmpty()){
        logger.info("file is not empty");
        String fileName = UUID.randomUUID().toString();
        String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
        con.setCloudinaryImagePublicId(fileName);
        con.setPicture(imageUrl);
        contactForm.setPicture(imageUrl);
        
    } else {
        logger.info("file is empty");
    }
        //end of image processing

    var updateCon = contactService.update(con);
    logger.info("updated contact {}", updateCon);
        
    model.addAttribute("message", Message.builder().content("Contact Updated Successfully").type(MessageType.green).build());
         
        return "redirect:/user/contacts/view/" + contactId;
      }
}
 