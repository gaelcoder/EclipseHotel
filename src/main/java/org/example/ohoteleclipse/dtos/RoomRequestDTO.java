package org.example.ohoteleclipse.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomRequestDTO {

    private Long id;

    private String roomNumber;

    private String type;

    private double price;
}
