package edu.uws.ii.springboot.repositories;

import edu.uws.ii.springboot.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IProductsRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
}
