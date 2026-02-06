package edu.uws.ii.springboot.services;

import edu.uws.ii.springboot.commands.employees.DeleteEmployeeCommand;
import edu.uws.ii.springboot.commands.employees.EditEmployeeCommand;
import edu.uws.ii.springboot.commands.employees.GetEmployeesCommand;
import edu.uws.ii.springboot.commands.employees.RegisterEmployeeCommand;
import edu.uws.ii.springboot.commands.warehouses.AssignEmployeeToWarehouse;
import edu.uws.ii.springboot.commands.warehouses.UnassignEmployeeFromWarehouse;
import edu.uws.ii.springboot.interfaces.IEmployeesService;
import edu.uws.ii.springboot.models.Employee;
import edu.uws.ii.springboot.repositories.IEmployeesRepository;
import edu.uws.ii.springboot.specifications.EmployeeSpecifications;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeesService implements IEmployeesService {

    private final IEmployeesRepository employeesRepository;

    public EmployeesService(IEmployeesRepository employeesRepository) {
        this.employeesRepository = employeesRepository;
    }

    @Override
    public Employee registerEmployee(RegisterEmployeeCommand command) {
        return null;
    }

    @Override
    public void deleteEmployee(DeleteEmployeeCommand command) {

    }

    @Override
    public void editEmployee(EditEmployeeCommand command) {

    }

    @Override
    public List<Employee> getEmployees(GetEmployeesCommand command) {
        return employeesRepository.findAll(EmployeeSpecifications.byFilter(command));
    }

}
