package com.f4k8.socialresearch.controller;


import com.f4k8.socialresearch.model.Person;
import com.f4k8.socialresearch.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class AnonController {

  @Autowired
  SecurityService securityService;

  @GetMapping("/registration")
  public String reg_form(Person person) {
    return "registration";
  }


  @PostMapping("/registration")
  public String savePerson(Person person, Model model) {


    boolean isRegistered = securityService.registerPerson(person);

    if (isRegistered){
      return "redirect:/login";
    }
    else
      model.addAttribute("errorMessage","Email already exists");
      return "registration";
  }

  @GetMapping("/login")
  public String login(Person person){
    return "login";
  }



}
