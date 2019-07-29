package com.example.demo.entities.DTO;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class AccountBalanceUpdateDto {
    private String accountNumber;
    private BigDecimal balance;


}
