package edu.uws.ii.springboot.repositories;

import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IDeliveriesRepository extends JpaRepository<Delivery, Long>, JpaSpecificationExecutor<Delivery> {
}
