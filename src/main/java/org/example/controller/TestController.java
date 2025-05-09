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

            @PostMapping("/test")
            public String handlePost(@RequestBody User user) {
                return "Hello, " + user.getName() + "!";
            }

    static class User {
        private String name;
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        // Getters (required for JSON serialization)
        public String getName() { return name; }
        public int getAge() { return age; }
    }

}
