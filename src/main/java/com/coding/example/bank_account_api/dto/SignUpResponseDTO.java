package com.coding.example.bank_account_api.dto;

public class SignUpResponseDTO {

    private Long customerId;

    private SignUpResponseDTO(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public static SignUpResponseDTOBuilder newBuilder() {
        return new SignUpResponseDTOBuilder();
    }

    public static class SignUpResponseDTOBuilder {

        private Long customerId;

        public SignUpResponseDTOBuilder setCustomerId(Long customerId) {
            this.customerId = customerId;
            return this;
        }

        public SignUpResponseDTO build() {
            return new SignUpResponseDTO(customerId);
        }
    }
}
