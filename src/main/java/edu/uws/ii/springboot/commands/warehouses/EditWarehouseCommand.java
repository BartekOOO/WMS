package edu.uws.ii.springboot.commands.warehouses;

import edu.uws.ii.springboot.models.Warehouse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditWarehouseCommand {

    private Long id;
    private String name;
    private String code;
    private String description;

    public EditWarehouseCommand() {

    }

    public EditWarehouseCommand configureWarehouse(Warehouse warehouse) {
        this.id = warehouse.getId();
        this.name = warehouse.getName();
        this.code = warehouse.getCode();
        this.description = warehouse.getDescription();
        return this;
    }
}
