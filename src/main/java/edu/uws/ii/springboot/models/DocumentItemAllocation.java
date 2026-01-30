package edu.uws.ii.springboot.models;

import edu.uws.ii.springboot.enums.DocumentTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "DocumentItemAllocations",
        indexes = {
                @Index(name = "ix_alloc_item", columnList = "documentItemId"),
                @Index(name = "ix_alloc_delivery", columnList = "deliveryId")
        })
@Getter
@Setter
public class DocumentItemAllocation implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DocumentId", nullable = false)
    private Document document;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8, name="DocumentType")
    private DocumentTypeEnum type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DocumentItemId", nullable = false)
    private DocumentItem documentItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DeliveryId", nullable = false)
    private Delivery delivery;

    @Positive
    @Column(nullable = false, precision = 19, scale = 6, name = "Quantity")
    private BigDecimal quantity;

    @Column(nullable = false, name="CreatedAt")
    private LocalDateTime createdAt;
}
