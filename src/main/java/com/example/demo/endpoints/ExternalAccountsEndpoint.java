package com.example.demo.endpoints;

import com.example.demo.dao.ExternalAccountsServiceImpl;
import com.example.demo.entities.DTO.ExternalAccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/externalAccounts")
public class ExternalAccountsEndpoint {
    private ExternalAccountsServiceImpl externalAccountsService;

    @Autowired
    public ExternalAccountsEndpoint(ExternalAccountsServiceImpl externalAccountsService) {
        this.externalAccountsService = externalAccountsService;
    }
    @GetMapping("/all")
    public ResponseEntity<ExternalAccountDto[]> getAll() {
        return new ResponseEntity<>(externalAccountsService.getAllExternalAccounts(), HttpStatus.OK);
    }
}

