package edu.uws.ii.springboot.commands.sectors;

import edu.uws.ii.springboot.models.Warehouse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSectorCommand {

    private String code;
    private Long id;
    private Long warehouseId;

    public GetSectorCommand() {

    }

    public GetSectorCommand whereWarehouseEquals(Warehouse  warehouse) {
        this.warehouseId = warehouse.getId();
        return this;
    }

    public GetSectorCommand whereCodeEquals(String code) {
        this.code = code;
        return this;
    }

    public GetSectorCommand whereIdEquals(Long id) {
        this.id = id;
        return this;
    }
}
