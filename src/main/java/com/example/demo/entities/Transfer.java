package com.example.demo.entities;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Builder
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Account sendingAccount;

    private BigDecimal amount;

    @OneToOne
    private Account targetAccount;

    private LocalDateTime dateOfSendingTransfer;

    private LocalDateTime dateOfPostingTransfer;

    private String status;

    private String emailAddress;

    private Boolean ifSendEmail;
}
