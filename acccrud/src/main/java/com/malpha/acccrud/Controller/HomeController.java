package com.malpha.acccrud.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public String home(){
        return "Hello, User!";
    }

    @GetMapping("/locked")
    public String locked(){
        return "locked";
    }
    
}
