package com.example.demo.entities.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ExternalAccountDto {
    private Long id;

    private String number;

    private BigDecimal money;

    private String currency;

    private String owner;

    private Boolean deleted;
}
