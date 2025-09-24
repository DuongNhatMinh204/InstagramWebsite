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
        return "login";
    }
    @GetMapping("/profile")
    public String profilePage() {return "profile";}

    @GetMapping("/register")
    public String registerPage() {return "register";}

    @GetMapping("/edit-profile")
    public String editProfilePage() {
        return "edit-profile";
    }

    @GetMapping("/admin")
    public String adminPage() {return "admin";}
}
