package com.example.foody.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "credit_cards")
public class CreditCard extends DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "number", length = 16, nullable = false)
    private String number;

    @Column(name = "holder_name", length = 100, nullable = false)
    private String holderName;

    @Column(name = "cvv", length = 3, nullable = false)
    private String cvv;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @OneToOne(mappedBy = "creditCard")
    private User user;
}
