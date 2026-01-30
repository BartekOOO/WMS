package edu.uws.ii.springboot.commands.addresses;

import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Warehouse;
import lombok.Getter;

@Getter
public class AssignWarehouseToAddressCommand {
    private Long addressId;
    private Long warehouseId;

    public AssignWarehouseToAddressCommand() {

    }

    public AssignWarehouseToAddressCommand configureWarehouse(Long addressId){
        this.addressId = addressId;
        return this;
    }

    public AssignWarehouseToAddressCommand configureWarehouse(Warehouse warehouse){
        this.warehouseId = warehouse.getId();
        return this;
    }

    public AssignWarehouseToAddressCommand configureAddress(Address address){
        this.addressId = address.getId();
        return this;
    }

    public AssignWarehouseToAddressCommand configureAddress(Long addressId){
        this.addressId = addressId;
        return this;
    }
}
