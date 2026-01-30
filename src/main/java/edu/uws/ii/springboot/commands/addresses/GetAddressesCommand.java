package edu.uws.ii.springboot.commands.addresses;

import edu.uws.ii.springboot.models.Customer;
import edu.uws.ii.springboot.models.Warehouse;
import lombok.Getter;

@Getter
public class GetAddressesCommand {
    private Long id;
    private Long customerId;
    private Long warehouseId;
    private Boolean IsMain;

    public GetAddressesCommand() {

    }

    public GetAddressesCommand whereIdEqual(Long id){
        this.id = id;
        return this;
    }

    public GetAddressesCommand whereCustomerEqual(Long customerId){
        this.customerId = customerId;
        return this;
    }

    public GetAddressesCommand whereCustomerEqual(Customer customer){
        this.customerId = customer.getId();
        return this;
    }

    public GetAddressesCommand whereWarehouseEqual(Long warehouseId){
        this.warehouseId = warehouseId;
        return this;
    }

    public GetAddressesCommand whereWarehouseEqual(Warehouse warehouse){
        this.warehouseId = warehouse.getId();
        return this;
    }

    public GetAddressesCommand whereIsMain(Boolean isMain){
        this.IsMain = isMain;
        return this;
    }
}
