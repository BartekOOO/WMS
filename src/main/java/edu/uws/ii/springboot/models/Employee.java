package edu.uws.ii.springboot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Employees")
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private Long id;

    @NotBlank
    @Column(name="FirstName")
    private String firstName;

    @NotBlank
    @Column(name="LastName")
    private String lastName;

    @NotBlank
    @Email
    @Column(name="Email")
    private String email;

    @Column(name="Phone")
    private String phone;

    @OneToOne(mappedBy = "employee", fetch = FetchType.LAZY)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "EmployeeWarehouses",
            joinColumns = @JoinColumn(name = "EmployeeId"),
            inverseJoinColumns = @JoinColumn(name = "WarehouseId")
    )
    private Set<Warehouse> warehouses = new HashSet<>();

    @ManyToMany(mappedBy = "responsibleEmployees", fetch = FetchType.LAZY)
    private Set<Document> responsibleDocuments = new HashSet<>();


    public Employee(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public Employee() {}
}
