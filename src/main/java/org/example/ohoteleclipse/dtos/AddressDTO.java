package org.example.ohoteleclipse.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {
    private Long id;

    private String zipCode;

    private String street;

    private String addressDetails;

    private String neighborhood;

    private String state;
}
