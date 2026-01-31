package edu.uws.ii.springboot.specifications;

import edu.uws.ii.springboot.commands.products.GetProductsCommand;
import edu.uws.ii.springboot.models.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {
    public static Specification<Product> byFilter(GetProductsCommand c) {
        return (root, query, cb) -> {
            var p = cb.conjunction();

            if (c.getId() != null) {
                p = cb.and(p, cb.equal(root.get("id"), c.getId()));
            }

            if (c.getSku() != null && !c.getSku().isBlank()) {
                String sku = c.getSku().trim().toLowerCase();
                p = cb.and(p, cb.like(cb.lower(root.get("sku")), sku + "%"));
            }


            if (c.getName() != null && !c.getName().isBlank()) {
                p = cb.and(p, cb.like(
                        cb.lower(root.get("name")),
                        "%" + c.getName().trim().toLowerCase() + "%"
                ));
            }

            if (c.getEan() != null && !c.getEan().isBlank()) {
                p = cb.and(p, cb.equal(root.get("ean"), c.getEan().trim()));
            }

            if (c.getBrand() != null && !c.getBrand().isBlank()) {
                p = cb.and(p, cb.like(
                        cb.lower(root.get("brand")),
                        "%" + c.getBrand().trim().toLowerCase() + "%"
                ));
            }

            if (c.getIsArchival() != null) {
                p = cb.and(p, cb.equal(root.get("isArchival"), c.getIsArchival()));
            }

            return p;
        };
    }
}
