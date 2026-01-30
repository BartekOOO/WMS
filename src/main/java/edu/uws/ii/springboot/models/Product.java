package edu.uws.ii.springboot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Products")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private Long id;

    @Lob
    @Column(name="PhotoContent")
    private byte[] photoContent;

    @Column(name="PhotoName")
    private String photoName;

    @Column(name="PhotoContentType")
    private String photoContentType;

    @NotBlank
    @Column(nullable = false, unique = true, length = 64, name="Sku")
    private String sku;

    @NotBlank
    @Column(nullable = false, length = 200, name="Name")
    private String name;

    @NotBlank
    @Column(nullable = false, length = 16, name="Unit")
    private String unit;

    @Column(length = 32,name="Ean")
    private String ean;

    @Column(name = "IsArchival")
    private boolean isArchival;

    @Column(name="Description")
    private String description;

    @Column(name="Brand")
    private String brand;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Property> properties;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Unit> units;


    public Product(){

    }

    public void addUnit(Unit u) {
        units.add(u);
        u.setProduct(this);
    }

    public void removeUnit(Unit u) {
        units.remove(u);
        u.setProduct(null);
    }

    public void addProperty(Property property) {
        properties.add(property);
    }

    public void removeProperty(Property property) {
        properties.remove(property);
    }

}
