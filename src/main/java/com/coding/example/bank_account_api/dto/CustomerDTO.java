package com.coding.example.bank_account_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CustomerDTO {

    @JsonIgnore
    private Long id;

    @NotNull
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;
    @NotNull
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%]).{6,20})")
    private String password;

    private CustomerDTO() {
    }

    private CustomerDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public static CustomerDTOBuilder newBuilder() {
        return new CustomerDTOBuilder();
    }

    public static class CustomerDTOBuilder {

        private Long id;
        private String email;
        private String password;

        public CustomerDTOBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public CustomerDTOBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public CustomerDTOBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public CustomerDTO build() {
            CustomerDTO customerDTO = new CustomerDTO(email, password);
            customerDTO.setId(id);
            return customerDTO;
        }
    }
}
