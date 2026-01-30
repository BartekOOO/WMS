package edu.uws.ii.springboot.repositories;

import edu.uws.ii.springboot.models.DocumentItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDocumentItemsRepository extends JpaRepository<DocumentItem, Long> {
}
