package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User {

    @Size(max = 100)
    @Column(name = "department")
    private String department;

    // Constructors
    public Admin() {}

    public Admin(String fullName, String email, String password,
                 String department) {
        this.setFullName(fullName);
        this.setEmail(email);
        this.setPassword(password);
        this.department = department;
    }


    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    @Override
    public List<String> getRoles() {
        return List.of("ROLE_ADMIN");
    }
}