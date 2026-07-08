package com.ys.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    private String id; // keeps the frontend's own uid() string ids

    @ManyToOne
    @JoinColumn(name = "firm_id", nullable = false)
    @JsonIgnore // don't send the whole Firm object back to the browser, just the product fields
    private Firm firm;

    private String name;
    private String category;
    private String hsn;
    private String unit;
    private double purchase;
    private double selling;
    private int stock;
    private int gst;
    private int minStock;
    private String brand;
    private String expiry; // stored as plain text (yyyy-MM-dd) to match the frontend's date input
    private String notes;
}
