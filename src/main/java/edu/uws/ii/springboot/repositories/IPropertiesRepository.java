package edu.uws.ii.springboot.repositories;

import edu.uws.ii.springboot.models.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPropertiesRepository extends JpaRepository<Property, Integer> {
}
