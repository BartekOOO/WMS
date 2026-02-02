package edu.uws.ii.springboot.specifications;

import edu.uws.ii.springboot.commands.sectors.GetSectorCommand;
import edu.uws.ii.springboot.models.Sector;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class SectorSpecifications {
    public static Specification<Sector> byFilter(GetSectorCommand c) {
        return (root, query, cb) -> {

            if (!Long.class.equals(query.getResultType()) && !long.class.equals(query.getResultType())) {
                root.fetch("warehouse", JoinType.LEFT);
                query.distinct(true);
            }

            var p = cb.conjunction();
            if (c == null) return p;

            if (c.getId() != null) {
                p = cb.and(p, cb.equal(root.get("id"), c.getId()));
            }

            if (c.getCode() != null && !c.getCode().trim().isEmpty()) {
                String code = c.getCode().trim().toLowerCase();
                p = cb.and(p, cb.like(cb.lower(root.get("code")), code));
            }

            return p;
        };
    }
}
