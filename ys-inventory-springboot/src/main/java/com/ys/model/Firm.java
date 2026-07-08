package com.ys.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "firms")
@Data // Lombok: auto-generates getters/setters/toString so we don't type them by hand
public class Firm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    private String firmName;
    private String propName;
    private String address;
    private String phone;
    private String gstin;
    private String email;

    @Column(nullable = false)
    private int billCounter = 1;
}
