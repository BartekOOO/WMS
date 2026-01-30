package edu.uws.ii.springboot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private Long id;

    @NotBlank
    @Column(length = 200, name="PropertyName")
    private String propertyName;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "ProductId")
    private Product product;


    public Property() {

    }
}
