package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String sendingAccountNumber;

    private BigDecimal amount;

    private String targetAccountNumber;

    private LocalDateTime dateOfSendingTransfer;

    private LocalDateTime dateOfPostingTransfer;

    private String status;

    public enum status {
        PENDING, COMPLETED
    }
}
