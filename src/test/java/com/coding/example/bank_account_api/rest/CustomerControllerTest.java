package com.coding.example.bank_account_api.rest;

import com.coding.example.bank_account_api.dto.CustomerDTO;
import com.coding.example.bank_account_api.exceptions.ConstraintViolationException;
import com.coding.example.bank_account_api.exceptions.EntityNotFoundException;
import com.coding.example.bank_account_api.service.CustomerService;
import com.google.gson.Gson;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    private static final String FIRST_NAME = "aName";
    private static final String LAST_NAME = "aLastName";
    private static final String EMAIL = "customer@mail.com";
    private static final String PASSWORD = "1Valid@password";

    private static final String INVALID_EMAIL = "nonValidEmail.com";
    private static final String LESS_THAN_6_CHARACTERS_PASSWORD = "short";
    private static final String NO_UPPERCASE_PASSWORD = "1apassword!";
    private static final String NO_LOWERCASE_PASSWORD = "1APASSWORD!";
    private static final String NO_SPECIAL_SYMBOL_PASSWORD = "1aPassword";
    private static final String TOO_LONG_PASSWORD = "1aPasswordThatIsTooLongToBeValid!";

    private static final Long CUSTOMER_ID = 1L;
    private static final String ERROR_MESSAGE = "An error message";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerServiceMock;

    private Gson gson = new Gson();

    @Test
    public void signUp_whenAllCorrect_returnsStatusCreated() throws Exception {
        CustomerDTO customerDTO = buildValidCustomerDTO();
        when(customerServiceMock.create(argThat(new CustomerDTOMatcher(customerDTO)))).thenReturn(CUSTOMER_ID);

        this.mockMvc.perform(post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(customerDTO, CustomerDTO.class)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.customerId").value(CUSTOMER_ID));

        verify(customerServiceMock, times(1)).create(argThat(new CustomerDTOMatcher(customerDTO)));
    }

    @Test
    public void signUp_whenEmailIsNotValid_returnsBadRequest() throws Exception {
        CustomerDTO customerDTO = buildCustomerDTO(INVALID_EMAIL, PASSWORD);

        this.mockMvc.perform(post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(customerDTO, CustomerDTO.class)))
                .andExpect(status().isBadRequest());

        verify(customerServiceMock, times(0)).create(any(CustomerDTO.class));
    }

    @Test
    public void signUp_whenPasswordIsTooShort_returnsBadRequest() throws Exception {
        CustomerDTO customerDTO = buildCustomerDTO(EMAIL, LESS_THAN_6_CHARACTERS_PASSWORD);

        this.mockMvc.perform(post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(customerDTO, CustomerDTO.class)))
                .andExpect(status().isBadRequest());

        verify(customerServiceMock, times(0)).create(any(CustomerDTO.class));
    }

    @Test
    public void signUp_whenPasswordHasNoUppercase_returnsBadRequest() throws Exception {
        CustomerDTO customerDTO = buildCustomerDTO(EMAIL, NO_UPPERCASE_PASSWORD);

        this.mockMvc.perform(post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(customerDTO, CustomerDTO.class)))
                .andExpect(status().isBadRequest());

        verify(customerServiceMock, times(0)).create(any(CustomerDTO.class));
    }

    @Test
    public void signUp_whenPasswordHasNoLowercase_returnsBadRequest() throws Exception {
        CustomerDTO customerDTO = buildCustomerDTO(EMAIL, NO_LOWERCASE_PASSWORD);

        this.mockMvc.perform(post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(customerDTO, CustomerDTO.class)))
                .andExpect(status().isBadRequest());

        verify(customerServiceMock, times(0)).create(any(CustomerDTO.class));
    }

    @Test
    public void signUp_whenPasswordHasNoSpecialSymbol_returnsBadRequest() throws Exception {
        CustomerDTO customerDTO = buildCustomerDTO(EMAIL, NO_SPECIAL_SYMBOL_PASSWORD);

        this.mockMvc.perform(post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(customerDTO, CustomerDTO.class)))
                .andExpect(status().isBadRequest());

        verify(customerServiceMock, times(0)).create(any(CustomerDTO.class));
    }

    @Test
    public void signUp_whenPasswordIsTooLong_returnsBadRequest() throws Exception {
        CustomerDTO customerDTO = buildCustomerDTO(EMAIL, TOO_LONG_PASSWORD);

        this.mockMvc.perform(post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(customerDTO, CustomerDTO.class)))
                .andExpect(status().isBadRequest());

        verify(customerServiceMock, times(0)).create(any(CustomerDTO.class));
    }

    @Test
    public void signUp_whenEmailAlreadyExists_returnsBadRequest() throws Exception {
        CustomerDTO customerDTO = buildValidCustomerDTO();
        when(customerServiceMock.create(argThat(new CustomerDTOMatcher(customerDTO)))).thenThrow(new ConstraintViolationException(ERROR_MESSAGE));

        this.mockMvc.perform(post("/api/v1/customer")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(customerDTO, CustomerDTO.class)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(customerServiceMock, times(1)).create(argThat(new CustomerDTOMatcher(customerDTO)));
    }

    @Test
    public void findByEmail_whenFound_returnsCustomerDTO() throws Exception {
        CustomerDTO customerDTO = buildValidCustomerDTO();
        when(customerServiceMock.findByEmail(EMAIL)).thenReturn(customerDTO);

        this.mockMvc.perform(get("/api/v1/customer").param("email", EMAIL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(LAST_NAME))
                .andExpect(jsonPath("$.email").value(EMAIL));

        verify(customerServiceMock, times(1)).findByEmail(EMAIL);
    }

    @Test
    public void findByEmail_whenNotFound_returnsBadRequest() throws Exception {
        when(customerServiceMock.findByEmail(EMAIL)).thenThrow(new EntityNotFoundException(ERROR_MESSAGE));

        this.mockMvc.perform(get("/api/v1/customer").param("email", EMAIL))
                .andExpect(status().isBadRequest());

        verify(customerServiceMock, times(1)).findByEmail(EMAIL);
    }

    private CustomerDTO buildValidCustomerDTO() {
        return buildCustomerDTO(EMAIL, PASSWORD);
    }

    private CustomerDTO buildCustomerDTO(String email, String password) {
        CustomerDTO.CustomerDTOBuilder builder = CustomerDTO.newBuilder()
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .setEmail(email)
                .setPassword(password);

        return builder.build();
    }

    private class CustomerDTOMatcher extends ArgumentMatcher<CustomerDTO> {

        private CustomerDTO expected;

        public CustomerDTOMatcher(CustomerDTO expected) {
            this.expected = expected;
        }

        @Override
        public boolean matches(Object o) {
            if (o instanceof CustomerDTO) {
                CustomerDTO actual = (CustomerDTO) o;
                return EqualsBuilder.reflectionEquals(expected, actual);
            }
            return false;
        }
    }
}