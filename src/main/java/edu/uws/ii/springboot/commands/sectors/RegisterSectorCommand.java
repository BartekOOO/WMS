package edu.uws.ii.springboot.commands.sectors;

import edu.uws.ii.springboot.models.Sector;
import edu.uws.ii.springboot.models.Warehouse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterSectorCommand {

    private Sector sector;
    private Long warehouseId;

    public RegisterSectorCommand() {
        sector = new Sector();
    }

    public RegisterSectorCommand configureSector(Sector sector) {
        this.sector = sector;
        return this;
    }

    public RegisterSectorCommand configureWarehouse(Warehouse warehouse) {
        this.warehouseId = warehouse.getId();
        return this;
    }

    public RegisterSectorCommand configureWarehouse(Long warehouseId) {
        this.warehouseId = warehouseId;
        return this;
    }
}
