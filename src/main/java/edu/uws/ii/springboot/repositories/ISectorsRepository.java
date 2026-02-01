package edu.uws.ii.springboot.repositories;

import edu.uws.ii.springboot.models.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ISectorsRepository extends JpaRepository<Sector, Long>, JpaSpecificationExecutor<Sector> {

}
