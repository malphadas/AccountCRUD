package com.malpha.acccrud.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home(){
        return "Home.html";
    }

    @GetMapping("/register")
    public String register(){
        return "Register.html";
    }

    @GetMapping("/login")
    public String login(){
        return "Login.html";
    }
    
    @GetMapping("/admin")
    public String admin(){
        return "UserList.html";
    }

    @GetMapping("/{id}")
    public static String userPage(){
        return "UserPage.html";
    }

}
