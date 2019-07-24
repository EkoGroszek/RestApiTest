package com.example.demo.entities;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
public class ExternalTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Account sendingAccount;

    private BigDecimal amount;

    private String targetAccount;

    private LocalDateTime dateOfSendingTransfer;
}
