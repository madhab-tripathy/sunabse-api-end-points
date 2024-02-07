package com.sunbase.sunbase.user.management.repository;

import com.sunbase.sunbase.user.management.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByEmailIgnoreCase(String email);
    Optional<Customer> findByFirstNameIgnoreCase(String firstName);
    Optional<Customer> findByCityIgnoreCase(String city);
    Optional<Customer> findByPhone(String phone);
    Optional<Customer> findByUuid(String uuid);

    List<Customer> findAllByOrderByFirstNameAscLastNameAsc();
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByUuid(String uuid);
    void deleteByUuid(String uuid);

    @Query("SELECT c FROM Customer c WHERE " +
            "LOWER(c.firstName) LIKE CONCAT('%',LOWER(:query),'%') ")
    List<Customer> searchCustomerByName(String query);
    @Query("SELECT c FROM Customer c WHERE " +
            "LOWER(c.email) LIKE CONCAT('%',LOWER(:query),'%') ")
    List<Customer> searchCustomerByEmail(String query);
    @Query("SELECT c FROM Customer c WHERE " +
            "c.phone LIKE CONCAT('%',:query,'%') ")
    List<Customer> searchCustomerByPhone(String query);
    @Query("SELECT c FROM Customer c WHERE " +
            "LOWER(c.city) LIKE CONCAT('%',LOWER(:query),'%') ")
    List<Customer> searchCustomerByCity(String query);
}
