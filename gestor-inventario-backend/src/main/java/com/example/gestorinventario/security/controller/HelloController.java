package com.example.gestorinventario.security.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class HelloController {

    @GetMapping("/index")
    public String index(){
        System.out.println("Hello en el hello Controller en el sout");
        return "Hello desde hello controller en el return";
    }
}
