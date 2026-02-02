package edu.uws.ii.springboot.specifications;

import edu.uws.ii.springboot.commands.sectors.GetSectorCommand;
import edu.uws.ii.springboot.models.Sector;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class SectorSpecifications {
    public static Specification<Sector> byFilter(GetSectorCommand c) {
        return (root, query, cb) -> {

            // fetch only for entity queries (not count)
            if (!Long.class.equals(query.getResultType()) && !long.class.equals(query.getResultType())) {
                root.fetch("warehouse", JoinType.LEFT);
                query.distinct(true);
            }

            var p = cb.conjunction();
            if (c == null) return p;

            if (c.getId() != null) {
                p = cb.and(p, cb.equal(root.get("id"), c.getId()));
            }

            if (c.getWarehouseId() != null) {
                // Sector.warehouse.id = warehouseId
                p = cb.and(p, cb.equal(root.get("warehouse").get("id"), c.getWarehouseId()));
            }

            if (c.getCode() != null && !c.getCode().trim().isEmpty()) {
                String code = c.getCode().trim().toLowerCase();

                // jeśli user nie podał wildcardów, to robimy "contains"
                if (!code.contains("%") && !code.contains("_")) {
                    code = "%" + code + "%";
                }

                p = cb.and(p, cb.like(cb.lower(root.get("code")), code));
            }

            return p;
        };
    }
}
