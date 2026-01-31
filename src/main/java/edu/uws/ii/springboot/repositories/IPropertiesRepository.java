package edu.uws.ii.springboot.repositories;

import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IPropertiesRepository extends JpaRepository<Property, Integer>, JpaSpecificationExecutor<Property> {
}
