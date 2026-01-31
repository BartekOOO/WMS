package edu.uws.ii.springboot.specifications;

import edu.uws.ii.springboot.commands.products.properties.GetPropertiesCommand;
import edu.uws.ii.springboot.models.Property;
import org.springframework.data.jpa.domain.Specification;

public class PropertySpecifications {
    public static Specification<Property> byFilter(GetPropertiesCommand c) {
        return (root, query, cb) -> {
            var p = cb.conjunction();

            if (c == null) {
                return p;
            }

            if (c.getId() != null) {
                p = cb.and(p, cb.equal(root.get("id"), c.getId()));
            }

            if (c.getPropertyName() != null && !c.getPropertyName().isBlank()) {
                p = cb.and(p, cb.like(
                        cb.lower(root.get("propertyName")),
                        "" + c.getPropertyName().trim().toLowerCase() + ""
                ));
            }

            if (c.getProductId() != null) {
                p = cb.and(p, cb.equal(root.get("product").get("id"), c.getProductId()));
            }

            return p;
        };
    }
}
