package org.ebanking.controller;

import org.ebanking.dto.response.UserResponse;
import org.ebanking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserDataController {

    @Autowired
    private UserService userService;

    @GetMapping("/details")
    public ResponseEntity<UserResponse> getUserDetails(
            @RequestHeader("user-id") Long userId) {
        UserResponse response = userService.getUserDetails(userId);
        return ResponseEntity.ok(response);
    }
}