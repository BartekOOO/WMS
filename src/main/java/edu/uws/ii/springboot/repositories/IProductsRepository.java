package edu.uws.ii.springboot.repositories;

import edu.uws.ii.springboot.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductsRepository extends JpaRepository<Product, Long> {
}
