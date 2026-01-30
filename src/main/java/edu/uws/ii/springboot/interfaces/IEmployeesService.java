package edu.uws.ii.springboot.interfaces;

import edu.uws.ii.springboot.models.Employee;

import java.util.List;

public interface IEmployeesService {
    void registerEmployee(Employee e);
    void deleteEmployee(int id);
    void editEmployee(Employee e);
    List<Employee> getEmployees();
    Employee getEmployee(int id);
}
