package org.example.ohoteleclipse.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ViaCepResponseDTO {
    private String cep;
    private String logradouro;
    private String bairro;
    private String uf;
    private boolean erro;
}
