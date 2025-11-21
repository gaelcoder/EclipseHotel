package org.example.ohoteleclipse.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationCreateDTO {
    @NotNull(message = "O ID do cliente é obrigatório.")
    private Long customerId;

    @NotNull(message = "O ID do quarto é obrigatório.")
    private Long roomId;

    @NotNull(message = "A data de check-in é obrigatória.")
    @Future(message = "A data de check-in deve ser uma data futura.")
    private LocalDateTime checkin;

    @NotNull(message = "A data de checkout é obrigatória.")
    @Future(message = "A data de checkout deve ser uma data futura.")
    private LocalDateTime checkout;

}
