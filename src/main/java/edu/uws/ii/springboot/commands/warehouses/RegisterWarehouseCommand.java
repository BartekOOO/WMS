package edu.uws.ii.springboot.commands.warehouses;


import edu.uws.ii.springboot.enums.SectorTypeEnum;
import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Employee;
import edu.uws.ii.springboot.models.Sector;
import edu.uws.ii.springboot.models.Warehouse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterWarehouseCommand {
    private Warehouse warehouse;
    private Address address;
    private Long addressId;

    private Sector unloadingSector;
    private Sector loadingSector;
    private Sector sector;

    public RegisterWarehouseCommand() {
        this.warehouse = new Warehouse();
        unloadingSector = new Sector();
        loadingSector = new Sector();
        sector = new Sector();
        sector.setType(SectorTypeEnum.Normal);
        loadingSector.setType(SectorTypeEnum.LoadingHub);
        unloadingSector.setType(SectorTypeEnum.UnloadingHub);
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
