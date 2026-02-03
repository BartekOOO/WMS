package edu.uws.ii.springboot.commands.warehouses;


import edu.uws.ii.springboot.models.Employee;
import edu.uws.ii.springboot.models.Warehouse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignEmployeeToWarehouse {

    private Long employeeId;
    private Long warehouseId;

    public AssignEmployeeToWarehouse() {

    }

    public AssignEmployeeToWarehouse configureEmployee(Employee employee) {
        this.employeeId = employee.getId();
        return this;
    }

    public AssignEmployeeToWarehouse configureEmployee(Long employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public AssignEmployeeToWarehouse configureWarehouse(Warehouse warehouse) {
        this.warehouseId = warehouse.getId();
        return this;
    }

    public AssignEmployeeToWarehouse configureWarehouse(Long warehouseId) {
        this.warehouseId = warehouseId;
        return this;
    }
}
