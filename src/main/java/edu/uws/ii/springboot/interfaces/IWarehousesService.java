package edu.uws.ii.springboot.interfaces;

import edu.uws.ii.springboot.commands.sectors.DeleteSectorCommand;
import edu.uws.ii.springboot.commands.sectors.EditSectorCommand;
import edu.uws.ii.springboot.commands.sectors.GetSectorCommand;
import edu.uws.ii.springboot.commands.sectors.RegisterSectorCommand;
import edu.uws.ii.springboot.commands.warehouses.*;
import edu.uws.ii.springboot.models.Sector;
import edu.uws.ii.springboot.models.Warehouse;

import java.util.List;

public interface IWarehousesService {
    List<Warehouse> getWarehouses(GetWarehousesCommand command);
    Warehouse registerWarehouse(RegisterWarehouseCommand command);
    void editWarehouse(EditWarehouseCommand command);
    void assignEmployee(AssignEmployeeToWarehouse command);
    void unassignEmployee(UnassignEmployeeFromWarehouse command);
    void deleteWarehouse(DeleteWarehouseCommand command);

    Sector registerSector(RegisterSectorCommand command);
    List<Sector> getSectors(GetSectorCommand command);
    void deleteSector(DeleteSectorCommand command);
    void editSector(EditSectorCommand command);

    Warehouse getWarehouseDetails(Long id);
}
