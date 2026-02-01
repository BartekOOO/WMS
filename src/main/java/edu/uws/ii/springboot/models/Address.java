package edu.uws.ii.springboot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;


@Getter
@Setter
@Entity
@Table(name="Addresses")
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @NotBlank
    @Column(name = "Street")
    private String street;

    @NotBlank
    @Column(name = "City")
    private String city;

    @Column(name = "State")
    private String state;

    @NotBlank
    @Column(name = "ZipCode")
    private String zipCode;

    @NotBlank
    @Column(name = "Country")
    private String country;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "CustomerId")
    private Customer customer;

    @Column(name = "IsMainAddress")
    private boolean isMainAddress;

    @Column(name = "IsArchival")
    private boolean isArchival;

    public Address(String street, String city, String state, String zipCode, String country)
    {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    public Address(){

    }

}
