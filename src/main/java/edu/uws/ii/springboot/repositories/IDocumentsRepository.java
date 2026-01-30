package edu.uws.ii.springboot.repositories;

import edu.uws.ii.springboot.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDocumentsRepository extends JpaRepository<Document, Long> {
}
