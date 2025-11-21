package org.example.ohoteleclipse.dtos;

import lombok.Data;

@Data
public class CustomerUpdateDTO {
    private String name;
    private String email;
    private String phone;
    private String zipCode;
    private String addressDetails;
}
