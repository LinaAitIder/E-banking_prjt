package org.example.controller;


import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class TestController {

        @GetMapping("/hello")
        public String test() {
            return "Backend is connected!";
        }


}
