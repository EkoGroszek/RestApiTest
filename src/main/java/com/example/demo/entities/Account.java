package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.jws.soap.SOAPBinding;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.util.Currency;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 26, max = 26)
    private String accountNumber;

    @Min(0)
    private BigDecimal balance;

    @ManyToOne
    private User owner;

    private String currency;

    private String name;


    public Account(@Size(min = 26, max = 26) String accountNumber, @Min(0) BigDecimal balance, User owner, String currency, String name) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.owner = owner;
        this.currency = currency;
        this.name = name;
    }
}
