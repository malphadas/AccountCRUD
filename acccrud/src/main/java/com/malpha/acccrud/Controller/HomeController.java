package com.malpha.acccrud.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home(){
        return "home.html";
    }

    @GetMapping("/locked")
    public String locked(){
        return "locked";
    }
    
}
