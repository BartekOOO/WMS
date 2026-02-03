package edu.uws.ii.springboot.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "Deliveries")
public class Delivery implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ProductId", nullable = false)
    private Product product;

    @Column(name="Unit")
    private String unit;

    @Column(name="UnitMultiplier")
    private BigDecimal unitMultiplier;

    @Column(name="PropertyName")
    private String propertyName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "WarehouseId", nullable = false)
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SectorId")
    private Sector sector;

    @Positive
    @Column(nullable = false, name="Quantity")
    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CreatedByDocumentId")
    private Document createdByDocument;


    public Delivery(){}

}
