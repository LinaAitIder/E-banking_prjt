package org.ebanking.service;

import org.ebanking.dto.response.UserResponse;
import org.ebanking.model.User;

public interface UserService {
    UserResponse getUserDetails(Long userId);
    void enableWebAuthn(User user);

}