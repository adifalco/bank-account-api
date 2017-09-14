package com.coding.example.bank_account_api.service;

import com.coding.example.bank_account_api.domain.Customer;
import com.coding.example.bank_account_api.dto.CustomerDTO;
import com.coding.example.bank_account_api.exceptions.ConstraintViolationException;
import com.coding.example.bank_account_api.repository.CustomerRepository;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCustomerServiceTest {

    private static final Long ID = 1L;
    private static final String FIRST_NAME = "aName";
    private static final String LAST_NAME = "aLastName";
    private static final String EMAIL = "customer@mail.com";
    private static final String PASSWORD = "1Valid@password";

    private static final String ERROR_MESSAGE = "An error message";


    @Mock
    private CustomerRepository customerRepositoryMock;
    @InjectMocks
    private DefaultCustomerService customerService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void create_whenEmailDoesNotExist_returnsCustomerWithId() throws Exception {
        //Given
        CustomerDTO customerDTO = buildCustomerDTO();
        Customer customerMock = buildCustomer(customerDTO);

        Answer customerWithId = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Customer newCustomer = (Customer) invocationOnMock.getArguments()[0];
                ReflectionTestUtils.setField(newCustomer, "id", ID);
                return newCustomer;
            }
        };
        when(customerRepositoryMock.save(argThat(new CustomerMatcher(customerMock)))).thenAnswer(customerWithId);

        //When
        Long id = customerService.create(customerDTO);

        //Then
        assertEquals(ID, id);
    }

    @Test
    public void create_whenEmailAlreadyExists_returnsException() throws Exception {
        //Given
        CustomerDTO customerDTO = buildCustomerDTO();
        Customer customerMock = buildCustomer(customerDTO);

        Answer customerWithId = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Customer newCustomer = (Customer) invocationOnMock.getArguments()[0];
                ReflectionTestUtils.setField(newCustomer, "id", ID);
                return newCustomer;
            }
        };
        when(customerRepositoryMock.save(argThat(new CustomerMatcher(customerMock)))).thenThrow(new DataIntegrityViolationException(ERROR_MESSAGE));

        //Then & When
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage(ERROR_MESSAGE);
        customerService.create(customerDTO);
    }

    private CustomerDTO buildCustomerDTO() {
        CustomerDTO.CustomerDTOBuilder builder = CustomerDTO.newBuilder()
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .setEmail(EMAIL)
                .setPassword(PASSWORD);

        return builder.build();
    }

    private Customer buildCustomer(CustomerDTO customerDTO) {
        return new Customer(customerDTO.getFirstName(), customerDTO.getLastName(), customerDTO.getEmail(), customerDTO.getPassword());
    }

    private class CustomerMatcher extends ArgumentMatcher<Customer> {

        Customer expected;

        public CustomerMatcher(Customer expected) {
            this.expected = expected;
        }

        @Override
        public boolean matches(Object o) {
            if (o instanceof Customer) {
                Customer actual = (Customer) o;
                return EqualsBuilder.reflectionEquals(expected, actual, "dateCreated");
            }
            return false;
        }
    }
}