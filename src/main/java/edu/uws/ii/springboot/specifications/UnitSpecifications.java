package edu.uws.ii.springboot.specifications;

import edu.uws.ii.springboot.commands.products.units.GetUnitsCommand;
import edu.uws.ii.springboot.models.Unit;
import org.springframework.data.jpa.domain.Specification;

public class UnitSpecifications {
    public static Specification<Unit> byFilter(GetUnitsCommand c) {
        return (root, query, cb) -> {
            var p = cb.conjunction();

            if (c == null) {
                return p;
            }

            if (c.getId() != null) {
                p = cb.and(p, cb.equal(root.get("id"), c.getId()));
            }

            if (c.getUnitName() != null && !c.getUnitName().isBlank()) {
                p = cb.and(p, cb.like(
                        cb.lower(root.get("unitName")),
                        "%" + c.getUnitName().trim().toLowerCase() + "%"
                ));
            }

            if (c.getProductId() != null) {
                p = cb.and(p, cb.equal(root.get("product").get("id"), c.getProductId()));
            }

            if (c.getMultiplier() != null) {
                p = cb.and(p, cb.equal(root.get("multiplier"), c.getMultiplier()));
            }

            return p;
        };
    }
}
