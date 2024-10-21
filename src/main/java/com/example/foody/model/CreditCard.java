package com.example.foody.model;

import com.example.foody.model.user.CustomerUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
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
    private CustomerUser customer;
}
