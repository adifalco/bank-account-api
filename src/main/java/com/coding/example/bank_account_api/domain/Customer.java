package com.coding.example.bank_account_api.domain;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = "customer",
        uniqueConstraints = @UniqueConstraint(name = "uc_email", columnNames = {"email"})
)
public class Customer {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    @Email
    private String email;
    @Column(nullable = false)
    private String password;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    public Customer() {
    }

    public Customer(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dateCreated = new Date();
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
