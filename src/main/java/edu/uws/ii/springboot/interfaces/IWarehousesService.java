package edu.uws.ii.springboot.interfaces;

import edu.uws.ii.springboot.commands.warehouses.*;
import edu.uws.ii.springboot.models.Warehouse;

import java.util.List;

public interface IWarehousesService {
    List<Warehouse> getWarehouses(GetWarehousesCommand command);
    Warehouse registerWarehouse(RegisterWarehouseCommand command);
    void editWarehouse(EditWarehouseCommand command);
    void assignEmployee(AssignEmployeeToWarehouse command);
    void unassignEmployee(UnassignEmployeeFromWarehouse command);

    void RegisterSector(Regis);
}
