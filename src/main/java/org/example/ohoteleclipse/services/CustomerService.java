package org.example.ohoteleclipse.services;


import org.example.ohoteleclipse.dtos.CustomerUpdateDTO;
import org.example.ohoteleclipse.dtos.ViaCepResponseDTO;
import org.example.ohoteleclipse.models.Address;
import org.example.ohoteleclipse.repositories.ReservationRepository;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ohoteleclipse.dtos.CustomerCreateDTO;
import org.example.ohoteleclipse.models.Customer;
import org.example.ohoteleclipse.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ViaCepService viaCepService;
    private final ReservationRepository reservationRepository;

    @Cacheable("customers")
    public List<Customer> findAll() {
        log.info("Buscando todos os clientes (sem cache).");
        return customerRepository.findAll();
    }

    @Cacheable(value = "customer", key = "#id")
    public Customer findById(Long id) {
        log.info("Buscando cliente com id: {} (sem cache).", id);
        return customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente não encontrado para o id: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado.");
                });
    }


    @Transactional
    @Caching(evict = { // usando pra limpar vários caches
            @CacheEvict(value = "customers", allEntries = true), // Invalida a lista inteira
            @CacheEvict(value = "customer", key = "#result.id") // Invalida o cliente específico recém-criado
    })
    public Customer create(CustomerCreateDTO dto) {

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            if (customerRepository.existsByEmail(dto.getEmail())) {
                log.error("Tentativa de criar cliente com email já existente: {}", dto.getEmail());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado.");
            }
        }

        if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
            if (customerRepository.existsByPhone(dto.getPhone())) {
                log.error("Tentativa de criar cliente com telefone já existente: {}", dto.getPhone());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone já cadastrado.");
            }

        }

        ViaCepResponseDTO cepResponse = viaCepService.findAddressByZipCode(dto.getZipCode());
        Address address = new Address();
        address.setZipCode(cepResponse.getCep());
        address.setStreet(cepResponse.getLogradouro());
        address.setNeighborhood(cepResponse.getBairro());
        address.setState(cepResponse.getUf());
        address.setAddressDetails(dto.getAddressDetails());


        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(address);

        Customer savedCustomer = customerRepository.save(customer);
        log.info("Cliente {} criado com sucesso com o id: {}", savedCustomer.getName(), savedCustomer.getId());
        return savedCustomer;
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "customers", allEntries = true),
            @CacheEvict(value = "customer", key = "#id")
    })
    public Customer update(Long id, CustomerUpdateDTO dto) {
        log.info("Iniciando atualização para o cliente de id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            Optional<Customer> customerByEmail = customerRepository.findByEmail(dto.getEmail());
            if (customerByEmail.isPresent() && !customerByEmail.get().getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já está em uso por outro cliente.");
            }
            customer.setEmail(dto.getEmail());
        }

        if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
            Optional<Customer> customerByPhone = customerRepository.findByPhone(dto.getPhone());
            if (customerByPhone.isPresent() && !customerByPhone.get().getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone já está em uso por outro cliente.");
            }
            customer.setPhone(dto.getPhone());
        }

        if (dto.getName() != null && !dto.getName().isBlank()) {
            customer.setName(dto.getName());
        }

        Address address = customer.getAddress();
        boolean addressChanged = false;

        if (dto.getZipCode() != null && !dto.getZipCode().isBlank()) {
            ViaCepResponseDTO cepResponse = viaCepService.findAddressByZipCode(dto.getZipCode());
            address.setZipCode(cepResponse.getCep());
            address.setStreet(cepResponse.getLogradouro());
            address.setNeighborhood(cepResponse.getBairro());
            address.setState(cepResponse.getUf());
            addressChanged = true;
        }

        if (dto.getAddressDetails() != null) {
            address.setAddressDetails(dto.getAddressDetails());
            addressChanged = true;
        }

        if(addressChanged) {
            customer.setAddress(address);
        }

        Customer updatedCustomer = customerRepository.save(customer);
        log.info("Cliente de id {} atualizado com sucesso.", id);
        return updatedCustomer;
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "customers", allEntries = true),
            @CacheEvict(value = "customer", key = "#id")
    })
    public void delete(Long id) {
        log.info("Iniciando processo de deleção para o cliente de id: {}", id);
        if (!customerRepository.existsById(id)) {
            log.error("Tentativa de deletar cliente não existente com id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado.");
        }
        if (reservationRepository.existsByCustomerId(id)) {
            log.error("Tentativa de deletar cliente de id {} com reservas existentes.", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível deletar um cliente com reservas existentes.");
        }

        customerRepository.deleteById(id);
        log.info("Cliente de id {} deletado com sucesso.", id);
    }


}
