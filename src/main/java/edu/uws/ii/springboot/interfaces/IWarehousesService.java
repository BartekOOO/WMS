package edu.uws.ii.springboot.interfaces;

import edu.uws.ii.springboot.commands.warehouses.GetWarehousesCommand;
import edu.uws.ii.springboot.models.Warehouse;

import java.util.List;

public interface IWarehousesService {
    List<Warehouse> getWarehouses(GetWarehousesCommand command);
}
