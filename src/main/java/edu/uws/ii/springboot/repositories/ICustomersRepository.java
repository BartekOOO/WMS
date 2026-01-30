package edu.uws.ii.springboot.repositories;

import edu.uws.ii.springboot.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ICustomersRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {


}
