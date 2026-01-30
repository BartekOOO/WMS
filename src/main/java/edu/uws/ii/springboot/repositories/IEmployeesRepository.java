package edu.uws.ii.springboot.repositories;

import edu.uws.ii.springboot.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmployeesRepository extends JpaRepository<Employee, Long> {
}
