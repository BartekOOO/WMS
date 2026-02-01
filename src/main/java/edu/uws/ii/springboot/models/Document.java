package edu.uws.ii.springboot.models;


import edu.uws.ii.springboot.enums.DocumentStatusEnum;
import edu.uws.ii.springboot.enums.DocumentTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Documents")
public class Document implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private Long id;

    @Column(nullable = false, name="Number")
    private Integer number;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false, length = 8, name="DocumentType")
    private DocumentTypeEnum type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16, name="DocumentStatus")
    private DocumentStatusEnum status;

    @Column(nullable = false, name="IssueDate")
    private LocalDateTime issueDate;

    @Column(nullable = false, length = 32, name="IssuedByAcronym")
    private String issuedByAcronym;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MainCustomerId")
    private Customer mainCustomer; //Czyli ten do którego idzie
    @Column(name="MainCustomerName") //DLA WZ to kontrahent któy kupił towar
    private String mainCustomerName;  //DLA PZ to MY
    @Column(name="MainCustomerAcronym") //Dla magazynowego to MY
    private String mainCustomerAcronym; //Dla sektorowego to MY



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MainAddressId")
    private Address mainAddress; //Domyślnie główny adres kontrahenta, ale kontrahent moze miec wiele adresow
    @Column(name="MainAddress")
    private String mainFullAddress;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SupplierCustomerId")
    private Customer supplierCustomer; //Czyli ten do który wysyła/zaopatrza
    @Column(name="SupplierCustomerName")  //DLA WZ to MY
    private String supplierCustomerName; //DLA PZ to kontrahent
    @Column(name="SupplierCustomerAcronym") //DLA magazynowego to MY
    private String supplierCustomerAcronym; //DLA Sektorowego to MY



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SupplierAddressId")
    private Address supplierAddress; //Domyślnie główny adres kontrahenta, ale kontrahent moze miec wiele adresow
    @Column(name="SupplierAddress")
    private String supplierFullAddress;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DestinationWarehouseId")
    private Warehouse destinationWarehouse; //Czyli docelowy magazyn
    @Column(name="DestinationWarehouseName")  //DLA PZ to MY
    private String destinationWarehouseName; //DLA WZ to NULL no bo to jakiś kontrahent?
    @Column(name="DestinationWarehouseAcronym")  //DLA Magazynowego to nasz jakis magazyn
    private String destinationWarehouseAcronym;  //DLa SEKTOROWEGO tez my



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SourceWarehouseId")
    private Warehouse sourceWarehouse; //Czyli źródłowy źródłowy
    @Column(name="SourceWarehouseName") //DLA WZ to MY
    private String sourceWarehouseName; //DLA PZ TO NULL BO OD JAKIEGO KONTR$EHNTA
    @Column(name="SourceWarehouseAcronym") //DLA SEKTOROWEGO to MY
    private String sourceWarehouseAcronym; //DLA MAGAZYNOWEGO to MY



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DestinationSectorId")
    private Sector destinationSector; //Czyli docelowy sektor
    @Column(name="DestinationSectorName") //DLA WZ NULL - one założą te dokumenty
    private String destinationSectorName; //DLA PZ NULL - one założą te dokumenty
    @Column(name="DestinationSectorCode") //DLA Magazynowego NULL - samo założy
    private String destinationSectorCode; //DLA Sektoroweg - docelowy sektor



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SourceSectorId")
    private Sector sourceSector; //Czyli źródłowy sektor
    @Column(name="SourceSectorName")   //DLA WZ NULL - one założą te dokumenty
    private String sourceSectorName;   //DLA PZ NULL - one założą te dokumenty
    @Column(name="SourceSectorCode")   //DLA Magazynowego NULL - samo założy
    private String sourceSectorCode;   //DLA Sektoroweg - docelowy sektor



    @Column(length = 500, name="Notes")
    private String notes;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentItem> items = new ArrayList<>();

    @Column(name="LockedBy")
    private String lockedBy;

    @Column(name = "SourceDocumentId")
    private Long sourceDocumentId;

    @Transient
    public String getDocumentNumber() {
        int month = issueDate.getMonthValue();
        int year  = issueDate.getYear();
        return String.format("%s-%d/%02d/%d",
                type.getDescription(), number, month, year);
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "DocumentEmployees",
            joinColumns = @JoinColumn(name = "DocumentId"),
            inverseJoinColumns = @JoinColumn(name = "EmployeeId")
    )
    private List<Employee> responsibleEmployees;



    //Zdefiniowane w https://docs.google.com/spreadsheets/d/1WWJqY0cP2U-fVBTSG3p0j_6xCXJQunrlI6akWA_h2JM/edit?gid=0#gid=0
    public Document() {

    }


    public void assignEmployee(Employee e) {
        if (e == null) return;
        responsibleEmployees.add(e);
        e.getResponsibleDocuments().add(this);
    }

    public void unassignEmployee(Employee e) {
        if (e == null) return;
        responsibleEmployees.remove(e);
        e.getResponsibleDocuments().remove(this);
    }

}