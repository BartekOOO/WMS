package edu.uws.ii.springboot.interfaces;

import edu.uws.ii.springboot.commands.employees.DeleteEmployeeCommand;
import edu.uws.ii.springboot.commands.employees.EditEmployeeCommand;
import edu.uws.ii.springboot.commands.employees.GetEmployeesCommand;
import edu.uws.ii.springboot.commands.employees.RegisterEmployeeCommand;
import edu.uws.ii.springboot.commands.warehouses.AssignEmployeeToWarehouse;
import edu.uws.ii.springboot.commands.warehouses.UnassignEmployeeFromWarehouse;
import edu.uws.ii.springboot.models.Employee;

import java.util.List;

public interface IEmployeesService {
    Employee registerEmployee(RegisterEmployeeCommand command);
    void deleteEmployee(DeleteEmployeeCommand command);
    void editEmployee(EditEmployeeCommand command);
    List<Employee> getEmployees(GetEmployeesCommand command);
}
