package org.example.ohoteleclipse.controllers;
import lombok.RequiredArgsConstructor;
import org.example.ohoteleclipse.dtos.CustomerCreateDTO;
import org.example.ohoteleclipse.dtos.CustomerUpdateDTO;
import org.example.ohoteleclipse.models.Customer;
import org.example.ohoteleclipse.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // POST /api/customers
    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody CustomerCreateDTO dto) {
        Customer newCustomer = customerService.create(dto);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    // GET /api/customers
    @GetMapping
    public ResponseEntity<List<Customer>> findAll() {
        List<Customer> customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }

    // GET /api/customers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Customer> findById(@PathVariable Long id) {
        Customer customer = customerService.findById(id);
        return ResponseEntity.ok(customer);
    }

    // PUT /api/customers/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody CustomerUpdateDTO dto) {
        Customer updatedCustomer = customerService.update(id, dto);
        return ResponseEntity.ok(updatedCustomer);
    }

    // DELETE /api/customers/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

