package com.sunbase.sunbase.user.management.service;


import com.sunbase.sunbase.user.management.dto.CustomerRequest;
import com.sunbase.sunbase.user.management.dto.CustomerResponse;

import java.util.UUID;

public interface CustomerService {
    String addCustomer(CustomerRequest customerRequest);
    String editCustomer(String uuid,CustomerRequest customerRequest);
    String findCustomerUUID(String email);
    CustomerResponse findCustomerById(String uuid);

    String deleteCustomerById(String uuid);
}
