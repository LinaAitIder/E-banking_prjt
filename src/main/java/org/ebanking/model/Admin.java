package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User {

    @Size(max = 100)
    @Column(name = "department")
    private String department;

    @Size(max = 50)
    @Column(name = "role")
    private String role;

    // Constructors
    public Admin() {}

    public Admin(String firstName, String lastName, String email, String password,
                 String department, String role) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setPassword(password);
        this.department = department;
        this.role = role;
    }

    // Getters/setters
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}