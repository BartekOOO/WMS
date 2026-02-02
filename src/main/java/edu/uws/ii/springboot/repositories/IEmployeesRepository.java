package edu.uws.ii.springboot.repositories;

import edu.uws.ii.springboot.models.Customer;
import edu.uws.ii.springboot.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IEmployeesRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
}
