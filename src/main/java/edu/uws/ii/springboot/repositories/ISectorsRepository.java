package edu.uws.ii.springboot.repositories;

import edu.uws.ii.springboot.models.Sector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISectorsRepository extends JpaRepository<Sector, Long> {
}
