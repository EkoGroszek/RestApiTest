package com.example.demo.entities.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class AccountBalanceUpdateDTO {
    private String accountNumber;
    private BigDecimal balance;

}
