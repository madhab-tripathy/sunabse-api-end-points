package com.sunbase.sunbase.user.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String email;
    private String phone;
}