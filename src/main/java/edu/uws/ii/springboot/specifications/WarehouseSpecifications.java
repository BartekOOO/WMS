package edu.uws.ii.springboot.specifications;

import edu.uws.ii.springboot.commands.warehouses.GetWarehousesCommand;
import edu.uws.ii.springboot.models.Warehouse;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class WarehouseSpecifications {

    public static Specification<Warehouse> byFilter(GetWarehousesCommand c) {
        return (root, query, cb) -> {

            if (!Long.class.equals(query.getResultType()) && !long.class.equals(query.getResultType())) {
                root.fetch("address", JoinType.LEFT);
                root.fetch("employees", JoinType.LEFT);

                query.distinct(true);
            }

            var p = cb.conjunction();

            if (c != null) {

                if (c.getCode() != null && !c.getCode().trim().isEmpty()) {
                    String code = c.getCode().trim().toLowerCase();
                    p = cb.and(p, cb.like(cb.lower(root.get("code")), "" + code + ""));
                }

                if (c.getName() != null && !c.getName().trim().isEmpty()) {
                    String name = c.getName().trim().toLowerCase();
                    p = cb.and(p, cb.like(cb.lower(root.get("name")), "" + name + ""));
                }

                if (c.getIsArchival() != null) {
                    p = cb.and(p, cb.equal(root.get("isArchival"), c.getIsArchival()));
                }

                if (c.getId() != null) {
                    p = cb.and(p, cb.equal(root.get("id"), c.getId()));
                }
            }

            return p;
        };
    }
}