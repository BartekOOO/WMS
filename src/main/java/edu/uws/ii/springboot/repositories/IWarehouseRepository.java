package edu.uws.ii.springboot.repositories;

import edu.uws.ii.springboot.models.Unit;
import edu.uws.ii.springboot.models.Warehouse;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface IWarehouseRepository extends JpaRepository<Warehouse, Long>, JpaSpecificationExecutor<Warehouse> {
    @EntityGraph(attributePaths = {"address", "employees"})
    Optional<Warehouse> findDetailsById(Long id);
}
