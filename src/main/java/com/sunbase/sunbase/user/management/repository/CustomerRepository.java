package com.sunbase.sunbase.user.management.repository;

import com.sunbase.sunbase.user.management.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByFirstName(String firstName);
    Optional<Customer> findByCity(String city);
    Optional<Customer> findByPhone(String phone);
    Optional<Customer> findByUuid(String uuid);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByUuid(String uuid);
    void deleteByUuid(String uuid);
}
