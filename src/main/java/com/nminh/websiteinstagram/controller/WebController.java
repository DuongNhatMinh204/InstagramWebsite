package com.nminh.websiteinstagram.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/home")
    public String homePage(){
        return "/home" ;
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // src/main/resources/templates/login.html
    }
    @GetMapping("/profile")
    public String profilePage() {return "profile";}
}
