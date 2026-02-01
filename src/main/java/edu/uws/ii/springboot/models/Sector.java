package edu.uws.ii.springboot.models;

import edu.uws.ii.springboot.enums.SectorTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "Sectors",
        uniqueConstraints = @UniqueConstraint(name = "ux_sector_warehouse_code", columnNames = {"WarehouseId", "Code"}))
public class Sector implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "WarehouseId", nullable = false)
    private Warehouse warehouse;

    @NotBlank
    @Column(nullable = false, length = 32, name="Code")
    private String code;

    @NotBlank
    @Column(length = 200, name="Name")
    private String name;

    @Column(name="Type")
    @NotNull
    @Enumerated(EnumType.STRING)
    private SectorTypeEnum type;
}
