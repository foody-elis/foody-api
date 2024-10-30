package com.example.foody.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
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

    @Column(name = "street", length = 30, nullable = false)
    private String street;

    @Column(name = "civic_number", length = 10, nullable = false)
    private String civicNumber;

    @Column(name = "postal_code", length = 5, nullable = false)
    private String postalCode;

    @OneToOne(mappedBy = "address")
    private Restaurant restaurant;

    public Address() {
    }

    public Address(long id, String city, String province, String street, String civicNumber, String postalCode, Restaurant restaurant) {
        this.id = id;
        this.city = city;
        this.province = province;
        this.street = street;
        this.civicNumber = civicNumber;
        this.postalCode = postalCode;
        this.restaurant = restaurant;
    }
}
