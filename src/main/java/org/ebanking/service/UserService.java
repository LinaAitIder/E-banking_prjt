package org.ebanking.service;

import org.ebanking.dto.response.UserResponse;

public interface UserService {
    UserResponse getUserDetails(Long userId);
}