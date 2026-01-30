package edu.uws.ii.springboot.repositories;

import edu.uws.ii.springboot.models.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWarehouseRepository extends JpaRepository<Warehouse, Long> {
}
