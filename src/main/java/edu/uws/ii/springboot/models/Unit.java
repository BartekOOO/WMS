package edu.uws.ii.springboot.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "Units")
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private Long id;

    @NotBlank
    @Column(length = 200, name="UnitName")
    private String unitName;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "ProductId")
    private Product product;

    @Positive
    @Column(name = "Multiplier", precision = 18, scale = 6, nullable = false)
    private BigDecimal multiplier = BigDecimal.ONE;

    public Unit() {

    }
}
