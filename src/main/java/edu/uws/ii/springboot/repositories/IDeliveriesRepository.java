package edu.uws.ii.springboot.repositories;

import edu.uws.ii.springboot.models.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDeliveriesRepository extends JpaRepository<Delivery, Long> {
}
