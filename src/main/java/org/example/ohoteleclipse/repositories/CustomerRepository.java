package org.example.ohoteleclipse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.ohoteleclipse.models.Customer;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByPhone(String phone);

}
