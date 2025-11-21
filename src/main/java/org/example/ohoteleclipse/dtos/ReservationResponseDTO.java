package org.example.ohoteleclipse.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDTO {

    private Long id;

    private LocalDateTime checkin;

    private LocalDateTime checkout;

    private String status;

    private Long customerId;
    private String customerName;

    private Long roomId;
    private String roomNumber;

}
