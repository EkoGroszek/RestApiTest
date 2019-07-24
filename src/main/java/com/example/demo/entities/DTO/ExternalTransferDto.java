package com.example.demo.entities.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ExternalTransferDto {

        private BigDecimal amount;

        private String bankName;

        private String currency;

        private String externalAccount;

        private String toAccount;
}
