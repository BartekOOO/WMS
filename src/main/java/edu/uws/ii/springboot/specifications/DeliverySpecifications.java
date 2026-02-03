package edu.uws.ii.springboot.specifications;

import edu.uws.ii.springboot.commands.deliveries.GetDeliveriesCommand;
import edu.uws.ii.springboot.models.Delivery;
import org.springframework.data.jpa.domain.Specification;

public class DeliverySpecifications {
    public static Specification<Delivery> byFilter(GetDeliveriesCommand c) {
        return (root, query, cb) -> {
            var p = cb.conjunction();

            if (c == null) return p;

            if (c.getId() != null) {
                p = cb.and(p, cb.equal(root.get("id"), c.getId()));
            }

            if (c.getWarehouseId() != null) {
                p = cb.and(p, cb.equal(root.get("warehouse").get("id"), c.getWarehouseId()));
            }

            if (c.getSectorId() != null) {
                p = cb.and(p, cb.equal(root.get("sector").get("id"), c.getSectorId()));
            }

            if (c.getProductId() != null) {
                p = cb.and(p, cb.equal(root.get("product").get("id"), c.getProductId()));
            }

            if (c.getProperty() != null && !c.getProperty().isBlank()) {
                p = cb.and(p, cb.equal(root.get("propertyName"), c.getProperty().trim()));
            }

            if (c.getQuantity() != null) {
                p = cb.and(p, cb.greaterThan(root.get("quantity"), c.getQuantity()));
            }

            return p;
        };
    }
}
