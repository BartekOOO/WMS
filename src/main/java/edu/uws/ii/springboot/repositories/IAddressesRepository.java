package edu.uws.ii.springboot.repositories;

import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface IAddressesRepository extends JpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {

}
