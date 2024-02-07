package com.sunbase.sunbase.user.management.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sunbase.sunbase.user.management.dto.CustomerRequest;
import com.sunbase.sunbase.user.management.dto.CustomerResponse;
import com.sunbase.sunbase.user.management.dto.SyncResponse;
import com.sunbase.sunbase.user.management.entity.Customer;
import com.sunbase.sunbase.user.management.repository.CustomerRepository;
import com.sunbase.sunbase.user.management.service.ApiSyncService;
import com.sunbase.sunbase.user.management.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository customerRepository;
    private final ApiSyncService apiSyncService;
    @Override
    public String addCustomer(CustomerRequest customerRequest) {
        Customer customer;
        String email = customerRequest.getEmail();
        String phone = customerRequest.getPhone();
//        checking for existing email and phone number
        if(customerRepository.existsByEmail(email)){
            return "invalid email";
        }
        if(customerRepository.existsByPhone(phone)){
            return "number already registered";
        }
        customer = Customer.builder()
                .uuid(UUID.randomUUID().toString())
                .firstName(customerRequest.getFirstName().toLowerCase())
                .lastName(customerRequest.getLastName().toLowerCase())
                .street(customerRequest.getStreet().toLowerCase())
                .address(customerRequest.getAddress().toLowerCase())
                .state(customerRequest.getState().toLowerCase())
                .city(customerRequest.getCity().toLowerCase())
                .phone(customerRequest.getPhone())
                .email(customerRequest.getEmail())
                .build();
//        save customer in customer table
        customerRepository.save(customer);
        return "customer added successfully";
    }

//    get customer by uuid
    private Customer getExistCustomer(String uuid){
        Optional<Customer> existingCustomer = customerRepository.findByUuid(uuid);
        return existingCustomer.orElse(null);
    }
//    update customer details
    @Override
    public String editCustomer(String uuid,CustomerRequest customerRequest){
//        get existing customer
        Customer customer = getExistCustomer(uuid);
        if(customer != null){
            customer.setFirstName(customerRequest.getFirstName().toLowerCase());
            customer.setLastName(customerRequest.getLastName().toLowerCase());
            customer.setStreet(customerRequest.getStreet().toLowerCase());
            customer.setAddress(customerRequest.getAddress().toLowerCase());
            customer.setState(customerRequest.getState().toLowerCase());
            customer.setCity(customerRequest.getCity().toLowerCase());
            customer.setPhone(customerRequest.getPhone());
            customer.setEmail(customerRequest.getEmail());
            customerRepository.save(customer);
        }else {
            throw new RuntimeException("Customer with email " + customerRequest.getEmail() + " not found");
        }
        return "edit successfully";
    }
    @Override
    public CustomerResponse findCustomerById(String uuid){
        Customer customer = getExistCustomer(uuid);
        CustomerResponse customerResponse = null;
        if (customer != null){
            customerResponse = CustomerResponse.builder()
                    .uuid(customer.getUuid())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .address(customer.getAddress())
                    .city(customer.getCity())
                    .state(customer.getState())
                    .email(customer.getEmail())
                    .phone(customer.getPhone())
                    .build();
        }
        return customerResponse;
    }
//     delete customer by id
    @Override
    @Transactional
    public String deleteCustomerById(String uuid) {
        Customer customer = getExistCustomer(uuid);
        if(customer != null){
            customerRepository.deleteByUuid(uuid);
            return "Customer deleted successfully";
        }else {
            throw new RuntimeException("Customer not exist");
        }
    }
//    synced data from the remote api, getting data from syncAPI service
    public List<CustomerResponse> syncedDataFromRemoteApi() throws JsonProcessingException {
        
        List<Map<String, Object>> data = apiSyncService.fetchCustomerData();
//        store synced data from remote api in list
        List<Object> syncResponses = new ArrayList<>();
        for (Map<String, Object> datum : data) {
            SyncResponse syncResponse = new SyncResponse();
            syncResponse.setFirstName((String) datum.get("first_name"));
            syncResponse.setLastName((String) datum.get("last_name"));
            syncResponse.setStreet((String) datum.get("street"));
            syncResponse.setAddress((String) datum.get("address"));
            syncResponse.setCity((String) datum.get("city"));
            syncResponse.setState((String) datum.get("street"));
            syncResponse.setEmail((String) datum.get("email"));
            syncResponse.setPhone((String) datum.get("phone"));
            syncResponse.setUuid((String) datum.get("uuid"));
            syncResponses.add(syncResponse);
        }
        List<CustomerResponse> customerResponses = new ArrayList<>();
        updateCustomerTableWithRemoteData(syncResponses);
    //          fetch all the data from database;
        List<Customer> customers = customerRepository.findAllByOrderByFirstNameAscLastNameAsc();
        for (Customer c : customers){
            if (c.getUuid() != null && !c.getUuid().isEmpty()) {
                CustomerResponse customerResponse = findCustomerById(c.getUuid());
                customerResponses.add(customerResponse);
            }
        }
        return customerResponses;
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findAllByOrderByFirstNameAscLastNameAsc();
        return findCustomerResponse(customers);
    }

    private void updateCustomerTableWithRemoteData(List<Object> syncResponse){
//      check if customer exist in the database or not, if not exist insert a new record
//        if present update it into our database
        for (Object key: syncResponse.stream().toList()) {
            SyncResponse c = (SyncResponse) key;
//            customer Request
            CustomerRequest customerRequest = new CustomerRequest(
                    c.getFirstName(),
                    c.getLastName(),
                    c.getStreet(),
                    c.getAddress(),
                    c.getCity(),
                    c.getState(),
                    c.getEmail(),
                    c.getPhone()
            );
//            Customer
            Customer customer = Customer.builder()
                    .uuid(c.getUuid())
                    .firstName(c.getFirstName())
                    .lastName(c.getLastName())
                    .street(c.getStreet())
                    .address(c.getAddress())
                    .city(c.getCity())
                    .state(c.getState())
                    .email(c.getEmail())
                    .phone(c.getPhone())
                    .build();

            System.out.println(c.getUuid());
            if(!customerRepository.existsByUuid(c.getUuid())){
                System.out.println(customerRequest.getFirstName());
//                create new customer
                addCustomer(customer);
            }else{
//                update with existing customer
                editCustomer(c.getUuid(),customerRequest);
            }
        }
    }
//    add customer for remote api data
    private void addCustomer(Customer customer){
        customer = Customer.builder()
                .uuid(customer.getUuid())
                .firstName(customer.getFirstName().toLowerCase())
                .lastName(customer.getLastName().toLowerCase())
                .street(customer.getStreet().toLowerCase())
                .address(customer.getAddress().toLowerCase())
                .state(customer.getState().toLowerCase())
                .city(customer.getCity().toLowerCase())
                .phone(customer.getPhone().toLowerCase())
                .email(customer.getEmail())
                .build();
//        save customer in customer table
        customerRepository.save(customer);
    }
//    getting customer response
    private List<CustomerResponse> findCustomerResponse(List<Customer> customers){
        List<CustomerResponse> customerResponses = new ArrayList<>();
        for(Customer c : customers){
            CustomerResponse customerResponse = CustomerResponse.builder()
                    .uuid(c.getUuid())
                    .firstName(c.getFirstName())
                    .lastName(c.getLastName())
                    .address(c.getAddress())
                    .city(c.getCity())
                    .state(c.getState())
                    .email(c.getEmail())
                    .phone(c.getPhone())
                    .build();
            customerResponses.add(customerResponse);
        }
        return customerResponses;
    }
//    search by search option and search query
    public List<CustomerResponse> getCustomersBySearch(String searchOption, String query){
        List<Customer> customers = customerRepository.findAllByOrderByFirstNameAscLastNameAsc();
        List<CustomerResponse> customerResponses;
//    Simple searching functionality by the user query
        switch (searchOption){
//            search by first name
            case "First Name":
                customers = customerRepository.searchCustomerByName(query);
                break;
//             search by city
            case "City":
                customers = customerRepository.searchCustomerByCity(query);
                break;
//                search by phone
            case "Phone":
                customers = customerRepository.searchCustomerByPhone(query);
                break;
//                search by email
            case "Email":
                customers = customerRepository.searchCustomerByEmail(query);
                break;
            default:
                break;
        }
//        all the customer who matches with search term in sorted by first and last name
        customerResponses = findCustomerResponse(customers);
        return customerResponses;
    }



}
