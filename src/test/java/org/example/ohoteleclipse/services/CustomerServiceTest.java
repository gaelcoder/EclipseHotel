package org.example.ohoteleclipse.services;

import org.example.ohoteleclipse.dtos.CustomerCreateDTO;
import org.example.ohoteleclipse.dtos.CustomerUpdateDTO;
import org.example.ohoteleclipse.dtos.ViaCepResponseDTO;
import org.example.ohoteleclipse.models.Customer;
import org.example.ohoteleclipse.repositories.CustomerRepository;
import org.example.ohoteleclipse.repositories.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ViaCepService viaCepService;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private CustomerService customerService;

    private CustomerCreateDTO customerCreateDTO;
    private ViaCepResponseDTO viaCepResponseDTO;

    @BeforeEach
    void setUp() {
        customerCreateDTO = new CustomerCreateDTO();
        customerCreateDTO.setName("John Doe");
        customerCreateDTO.setEmail("john.doe@example.com");
        customerCreateDTO.setPhone("1234567890");
        customerCreateDTO.setZipCode("01001000");
        customerCreateDTO.setAddressDetails("Apt 101");

        viaCepResponseDTO = new ViaCepResponseDTO();
        viaCepResponseDTO.setCep("01001-000");
        viaCepResponseDTO.setLogradouro("Praça da Sé");
        viaCepResponseDTO.setBairro("Sé");
        viaCepResponseDTO.setUf("SP");
    }

    @Test
    void create_shouldCreateCustomer_whenDataIsValide() {

        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.existsByPhone(anyString())).thenReturn(false);
        when(viaCepService.findAddressByZipCode(anyString())).thenReturn(viaCepResponseDTO);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer c = invocation.getArgument(0);
            c.setId(1L);
            return c;
        });

        Customer createdCustomer = customerService.create(customerCreateDTO);

        assertNotNull(createdCustomer);
        assertNotNull(createdCustomer.getId());
        assertEquals("John Doe", createdCustomer.getName());
        assertEquals("john.doe@example.com", createdCustomer.getEmail());
        assertNotNull(createdCustomer.getAddress());
        assertEquals("Praça da Sé", createdCustomer.getAddress().getStreet());
        assertEquals("Apt 101", createdCustomer.getAddress().getAddressDetails());

        verify(customerRepository).existsByEmail(customerCreateDTO.getEmail());
        verify(customerRepository).existsByPhone(customerCreateDTO.getPhone());
        verify(viaCepService).findAddressByZipCode(customerCreateDTO.getZipCode());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void create_shouldThrowException_whenEmailAlreadyExists() {

        when(customerRepository.existsByEmail(anyString())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            customerService.create(customerCreateDTO);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Email já cadastrado.", exception.getReason());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void findById_shouldThrowException_whenCustomerNotFound() {

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            customerService.findById(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void update_shouldUpdateCustomer_whenDataIsValid() {

        Customer existingCustomer = new Customer();
        existingCustomer.setId(1L);
        existingCustomer.setName("Old Name");
        existingCustomer.setEmail("old@example.com");

        CustomerUpdateDTO updateDTO = new CustomerUpdateDTO();
        updateDTO.setName("New Name");
        updateDTO.setEmail("new@example.com");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.findByEmail(updateDTO.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(existingCustomer);

        Customer updatedCustomer = customerService.update(1L, updateDTO);

        assertNotNull(updatedCustomer);
        assertEquals("New Name", updatedCustomer.getName());
        assertEquals("new@example.com", updatedCustomer.getEmail());
        verify(customerRepository).findById(1L);
        verify(customerRepository).save(existingCustomer);
    }

    @Test
    void delete_shouldThrowException_whenCustomerHasReservations() {

        Long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(reservationRepository.existsByCustomerId(customerId)).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            customerService.delete(customerId);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Não é possível deletar um cliente com reservas existentes.", exception.getReason());
        verify(customerRepository, never()).deleteById(anyLong());
    }

    @Test
    void delete_shouldDeleteCustomer_whenNoReservationsExist() {

        Long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(reservationRepository.existsByCustomerId(customerId)).thenReturn(false);
        doNothing().when(customerRepository).deleteById(customerId);

        assertDoesNotThrow(() -> customerService.delete(customerId));

        verify(customerRepository).deleteById(customerId);
    }
}