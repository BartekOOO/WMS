package edu.uws.ii.springboot.commands.warehouses;

import edu.uws.ii.springboot.models.Employee;
import edu.uws.ii.springboot.models.Warehouse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnassignEmployeeFromWarehouse {

    private Long employeeId;
    private Long warehouseId;

    public UnassignEmployeeFromWarehouse() {

    }

    public UnassignEmployeeFromWarehouse configureEmployee(Employee employee) {
        this.employeeId = employee.getId();
        return this;
    }

    public UnassignEmployeeFromWarehouse configureWarehouse(Warehouse warehouse) {
        this.warehouseId = warehouse.getId();
        return this;
    }
}
