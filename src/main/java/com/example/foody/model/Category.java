package com.example.foody.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 30, nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "categories")
    private List<Restaurant> restaurants = new ArrayList<>();

    public Category() {
    }

    public Category(long id, String name, List<Restaurant> restaurants) {
        this.id = id;
        this.name = name;
        this.restaurants = restaurants;
    }
}
