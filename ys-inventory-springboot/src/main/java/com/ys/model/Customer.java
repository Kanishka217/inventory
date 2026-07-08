package com.ys.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customers")
@Data
public class Customer {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "firm_id", nullable = false)
    @JsonIgnore
    private Firm firm;

    private String name;
    private String phone;
    private String email;
    private String gstin;
    private String address;
    private String contact;
}
