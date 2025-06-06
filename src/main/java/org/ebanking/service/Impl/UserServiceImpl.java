package org.ebanking.service.Impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.ebanking.dao.UserRepository;
import org.ebanking.dto.response.UserResponse;
import org.ebanking.model.Admin;
import org.ebanking.model.BankAgent;
import org.ebanking.model.Client;
import org.ebanking.model.User;
import org.ebanking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserResponse getUserDetails(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return convertToDto(user);
    }

    private UserResponse convertToDto(User user) {
        UserResponse dto = new UserResponse();

        // Champs communs
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setActive(user.getActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setWebAuthnEnabled(user.isWebAuthnEnabled());
        dto.setUserType(user.getClass().getSimpleName().toUpperCase());

        // Champs spécifiques
        if (user instanceof Client) {
            Client client = (Client) user;
            dto.setDateOfBirth(client.getDateOfBirth());
            dto.setNationalId(client.getNationalId());
            dto.setAddress(client.getAddress());
            dto.setCity(client.getCity());
            dto.setCountry(client.getCountry());
            dto.setTermsAccepted(client.getTermsAccepted());
            dto.setPhoneVerified(client.isPhoneVerified());
        }
        else if (user instanceof Admin) {
            Admin admin = (Admin) user;
            dto.setFirstLogin(admin.isFirstLogin());
            dto.setDepartment(admin.getDepartment());
        }
        else if (user instanceof BankAgent) {
            BankAgent agent = (BankAgent) user;
            dto.setAgentCode(agent.getAgentCode());
            dto.setAgency(agent.getAgency());
        }

        return dto;
    }
}