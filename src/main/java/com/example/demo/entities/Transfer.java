package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // TODO: 22.07.2019  przerobić na konta zamiast numerów kont
    private String sendingAccountNumber;

    private BigDecimal amount;

    private String targetAccountNumber;

    private LocalDateTime dateOfSendingTransfer;

    private LocalDateTime dateOfPostingTransfer;

    private String status;

    // TODO: 22.07.2019  wyekstraktować enum do osobnej klasy "transferStatus"

}
