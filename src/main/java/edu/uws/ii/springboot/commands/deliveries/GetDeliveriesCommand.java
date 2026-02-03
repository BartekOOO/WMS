package edu.uws.ii.springboot.commands.deliveries;

import edu.uws.ii.springboot.models.Product;
import edu.uws.ii.springboot.models.Property;
import edu.uws.ii.springboot.models.Sector;
import edu.uws.ii.springboot.models.Warehouse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GetDeliveriesCommand {

    private Long id;
    private Long warehouseId;
    private Long sectorId;
    private Long productId;
    private String property;
    private BigDecimal quantity;

    public GetDeliveriesCommand() {

    }

    public GetDeliveriesCommand whereQuantityGreaterThan(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public GetDeliveriesCommand wherePropertyEquals(String property) {
        this.property = property;
        return this;
    }

    public GetDeliveriesCommand wherePropertyEquals(Property property) {
        this.property = property.getPropertyName();
        return this;
    }

    public GetDeliveriesCommand whereProductEquals(Long productId) {
        this.productId = productId;
        return this;
    }

    public GetDeliveriesCommand whereProductEquals(Product product) {
        this.productId = product.getId();
        return this;
    }

    public GetDeliveriesCommand whereSectorEquals(Long sectorId) {
        this.sectorId = sectorId;
        return this;
    }

    public GetDeliveriesCommand whereSectorEquals(Sector sector) {
        this.sectorId = sector.getId();
        return this;
    }

    public GetDeliveriesCommand whereWarehouseEquals(Long warehouseId) {
        this.warehouseId = warehouseId;
        return this;
    }

    public GetDeliveriesCommand whereWarehouseEquals(Warehouse warehouse) {
        this.warehouseId = warehouse.getId();
        return this;
    }

    public GetDeliveriesCommand whereIdEquals(Long id)
    {
        this.id=id;
        return this;
    }
}
