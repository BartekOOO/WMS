package edu.uws.ii.springboot.models;


import edu.uws.ii.springboot.enums.DocumentTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "DocumentItems")
public class DocumentItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false, length = 8, name="DocumentType")
    private DocumentTypeEnum type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DocumentId", nullable = false)
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductId")
    private Product product;
    @Column(name="ProductName")
    private String productName;
    @Column(name="ProductCode")
    private String productCode;

    @Positive
    @Column(nullable = false, name="Quantity")
    private BigDecimal quantity;

    @OneToMany(mappedBy = "documentItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentItemAllocation> allocations;

    public DocumentItem() {

    }
}
