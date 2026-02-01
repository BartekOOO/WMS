package edu.uws.ii.springboot.models;

import edu.uws.ii.springboot.enums.CustomerTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter @Setter @AllArgsConstructor @Entity @Table(name="Customers")
public class Customer extends AuditedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "IsMain", nullable = false)
    private boolean isMain;

    @NotBlank
    @Column(name = "Acronym")
    private String acronym;

    @NotBlank
    @Column(name = "FullName")
    private String fullName;

    @NotBlank
    @Column(name = "Nip")
    private String nip;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "CustomerType")
    private CustomerTypeEnum customerType;

    @Column(name = "IsArchival")
    private boolean isArchival;

    @Column(name = "Url")
    private String url;

    @Column(name = "Email")
    private String email;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "FoundedOn")
    private LocalDate foundedOn;

    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Address> addresses;

    public Customer(){

    }

    public Customer(String acronym, String fullName, String nip, CustomerTypeEnum customerType, boolean isArchival, LocalDate foundedOn, String url, String email) {
        this.acronym = acronym;
        this.fullName = fullName;
        this.nip = nip;
        this.customerType = customerType;
        this.isArchival = isArchival;
        this.foundedOn = foundedOn;
        this.url = url;
        this.email = email;
    }


    @Transient
    public Address getMainAddress() {
        if (addresses == null) return null;
        return addresses.stream()
                .filter(a -> a != null && a.isMainAddress() && !a.isArchival())
                .findFirst()
                .orElse(null);
    }

    @Transient
    public String getMainAddressLine() {
        Address a = getMainAddress();
        if (a == null) return "—";

        String street = a.getStreet() == null ? "" : a.getStreet().trim();
        String zip    = a.getZipCode() == null ? "" : a.getZipCode().trim();
        String city   = a.getCity() == null ? "" : a.getCity().trim();

        String line = (street + ", " + zip + " " + city).replaceAll("\\s+", " ").trim();
        return line.equals(",") || line.equals("") ? "—" : line;
    }

    @Transient
    public String getCustomerTypeLabel() {
        return customerType == null ? "—" : customerType.getDescription();
    }

    public void addAddress(Address address) {
        addresses.add(address);
    }
}
