package com.sunbase.sunbase.user.management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "_customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "uuid")
    private String uuid;
    private String firstName;
    private String lastName;
    private String street;
    private String address;
    private String city;
    private String state;
    private String email;
    private String phone;
}
//class customeComparator implements Comparator<Customer>{
//
//    @Override
//    public int compare(Customer o1, Customer o2) {
//        return o1.getFirstName().compareToIgnoreCase(o2.getFirstName());
//    }
//}