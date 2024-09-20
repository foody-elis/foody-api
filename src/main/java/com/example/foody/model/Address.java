package com.example.foody.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "addresses")
public class Address extends DefaultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "city", length = 20, nullable = false)
    private String city;

    @Column(name = "province", length = 2, nullable = false)
    private String province;

    @Column(name = "road", length = 30, nullable = false)
    private String road;

    @Column(name = "civic_number", length = 10, nullable = false)
    private String civicNumber;

    @Column(name = "postal_code", length = 5, nullable = false)
    private String postalCode;

    @OneToOne(mappedBy = "address")
    private Restaurant restaurant;
}
