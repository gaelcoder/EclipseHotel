package org.example.ohoteleclipse.services;

import lombok.RequiredArgsConstructor;
import org.example.ohoteleclipse.dtos.ViaCepResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ViaCepService {

    private final RestTemplate restTemplate;

    public ViaCepResponseDTO findAddressByZipCode(String zipCode) {
        String viaCepUrl = "https://viacep.com.br/ws/" + zipCode + "/json/";
        try {
            ViaCepResponseDTO response = restTemplate.getForObject(viaCepUrl, ViaCepResponseDTO.class);
            if (response == null || response.isErro()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CEP não encontrado.");
            }
            return response;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao consultar o serviço ViaCep.", e);
        }
    }
}
