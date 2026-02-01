package edu.uws.ii.springboot.models;

import edu.uws.ii.springboot.enums.SectorTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Warehouses")
public class Warehouse implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private Long id;

    @NotBlank
    @Column(name="Code")
    private String code;

    @NotBlank
    @Column(name="Name")
    private String name;

    @Column(name="Description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AddressId")
    private Address address;

    @ManyToMany(mappedBy = "warehouses")
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sector> sectors;

    @Column(name="IsArchival")
    private boolean isArchival;


    public Warehouse() {
        sectors = new ArrayList<>();
    }

    public void addSector(Sector sector) {
        this.sectors.add(sector);
    }

    public void removeSector(Sector sector) {
        this.sectors.remove(sector);
    }

    public Sector getLoadingHub(){
        for(Sector s: sectors){
            if(s.getType() == SectorTypeEnum.LoadingHub)
                return s;
        }
        return null;
    }

    public Sector getUnloadingHub(){
        for(Sector s: sectors){
            if(s.getType() == SectorTypeEnum.UnloadingHub)
                return s;
        }
        return null;
    }
}
