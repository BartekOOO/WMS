package edu.uws.ii.springboot.commands.warehouses;


import edu.uws.ii.springboot.models.Warehouse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateWarehouseCommand {

    private Long id;
    private String code;
    private String name;
    private String description;

    public UpdateWarehouseCommand() {

    }

    public UpdateWarehouseCommand configureWarehouse(Warehouse warehouse) {
        this.id = warehouse.getId();
        this.code = warehouse.getCode();
        this.name = warehouse.getName();
        this.description = warehouse.getDescription();
        return this;
    }
}
