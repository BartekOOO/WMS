package edu.uws.ii.springboot.repositories;

import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface IUnitsRepository extends JpaRepository<Unit, Long>, JpaSpecificationExecutor<Unit> {
}
