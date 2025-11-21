package org.example.ohoteleclipse.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomCreateDTO {

    @NotBlank(message = "O número do quarto é necessário.")
    private String roomNumber;

    @NotBlank(message = "O tipo do quarto é necessário.")
    private String type;

    @NotNull(message = "O valor do quarto é obrigatório.")
    private double price;
}
