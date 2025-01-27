package com.example.foody.model;

import com.example.foody.model.user.CustomerUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "credit_cards")
@SQLRestriction("deleted_at IS NULL")
public class CreditCard extends DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "token", columnDefinition = "TEXT", unique = true)
    private String token;

    @OneToOne(mappedBy = "creditCard")
    private CustomerUser customer;
}