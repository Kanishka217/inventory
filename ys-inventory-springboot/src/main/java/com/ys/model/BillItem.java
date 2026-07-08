package com.ys.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bill_items")
@Data
public class BillItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = false)
    @JsonIgnore // avoid circular JSON (bill -> items -> bill -> items ...)
    private Bill bill;

    private String prodId;
    private String name;
    private String unit;
    private double qty;
    private double rate;
    private int gst;
    private double total;
}
