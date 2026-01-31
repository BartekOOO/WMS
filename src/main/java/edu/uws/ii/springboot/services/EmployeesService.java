package edu.uws.ii.springboot.services;

import edu.uws.ii.springboot.interfaces.IEmployeesService;
import edu.uws.ii.springboot.models.Employee;
import edu.uws.ii.springboot.repositories.IEmployeesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeesService implements IEmployeesService {

    private final IEmployeesRepository employeesRepository;

    public EmployeesService(IEmployeesRepository employeesRepository) {
        this.employeesRepository = employeesRepository;
    }



    @Override
    public void registerEmployee(Employee e) {

    }

    @Override
    public void deleteEmployee(int id) {

    }

    @Override
    public void editEmployee(Employee e) {

    }

    @Override
    public List<Employee> getEmployees() {
        return List.of();
    }

    @Override
    public Employee getEmployee(int id) {
        return null;
    }
}
