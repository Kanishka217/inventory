package com.ys.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bills")
@Data
public class Bill {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "firm_id", nullable = false)
    @JsonIgnore
    private Firm firm;

    private String billNo;
    private String date; // yyyy-MM-dd

    // Kept as a plain string (not a JPA relation) on purpose: a bill should
    // still show who it was billed to even if that customer is later edited
    // or deleted, so we snapshot the id + name at the time of billing.
    private String customerId;

    @JsonProperty("customer") // frontend's buildBill() calls this field "customer", not "customerName"
    private String customerName;

    private String address;
    private String gstin;
    private String payment;
    private double subtotal;
    private double gst;
    private double discount;
    private double grand;

    // cascade=ALL: saving/deleting a Bill automatically saves/deletes its items too.
    // orphanRemoval=true: removing an item from this list deletes it from the DB.
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillItem> items = new ArrayList<>();
}
