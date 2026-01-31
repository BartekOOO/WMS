package edu.uws.ii.springboot.commands.warehouses;


import edu.uws.ii.springboot.models.Warehouse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterWarehouseCommand {
    private Warehouse warehouse;

    public RegisterWarehouseCommand() {
        this.warehouse = new Warehouse();
    }

    public RegisterWarehouseCommand configureWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
        return this;
    }
}
