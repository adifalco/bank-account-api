package com.coding.example.bank_account_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO {

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;
    @NotNull
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%]).{6,20})")
    private String password;

    private CustomerDTO() {
    }

    private CustomerDTO(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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

    public static CustomerDTOBuilder newBuilder() {
        return new CustomerDTOBuilder();
    }

    public static class CustomerDTOBuilder {

        private String firstName;
        private String lastName;
        private String email;
        private String password;

        public CustomerDTOBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public CustomerDTOBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public CustomerDTOBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public CustomerDTOBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public CustomerDTO build() {
            return new CustomerDTO(firstName, lastName, email, password);
        }
    }
}
