package com.example.designdb.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.designdb.domain.customer.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByRrn(String rrn);
}
