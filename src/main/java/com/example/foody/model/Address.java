package com.example.foody.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

/**
 * Represents an address entity in the system.
 * <p>
 * Extends the {@link DefaultEntity} class and inherits its properties and methods.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "addresses")
@SQLRestriction("deleted_at IS NULL")
public class Address extends DefaultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "city", length = 20, nullable = false)
    private String city;

    @Column(name = "province", length = 2, nullable = false)
    private String province;

    @Column(name = "street", length = 30, nullable = false)
    private String street;

    @Column(name = "civic_number", length = 10, nullable = false)
    private String civicNumber;

    @Column(name = "postal_code", length = 5, nullable = false)
    private String postalCode;

    @OneToOne(mappedBy = "address")
    private Restaurant restaurant;
}