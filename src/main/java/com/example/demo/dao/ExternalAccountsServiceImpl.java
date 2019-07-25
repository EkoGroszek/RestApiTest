package com.example.demo.dao;

import com.example.demo.entities.DTO.ExternalAccountDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalAccountsServiceImpl {


    public ExternalAccountDto[] getAllExternalAccounts() {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "https://restapi97.herokuapp.com/api/accounts";
        ResponseEntity<ExternalAccountDto[]> response
                = restTemplate.getForEntity(fooResourceUrl, ExternalAccountDto[].class);

        return response.getBody();
    }
}
