package edu.uws.ii.springboot.commands.warehouses;


import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Warehouse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterWarehouseCommand {
    private Warehouse warehouse;
    private Address address;
    private Long addressId;

    public RegisterWarehouseCommand() {
        this.warehouse = new Warehouse();
    }

    public RegisterWarehouseCommand configureWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
        return this;
    }

    public RegisterWarehouseCommand configureAddress(Address address) {
        this.address = address;
        return this;
    }

    public RegisterWarehouseCommand configureAddressId(Long addressId) {
        this.addressId = addressId;
        return this;
    }
}
