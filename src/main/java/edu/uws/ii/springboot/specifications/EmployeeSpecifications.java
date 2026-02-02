package edu.uws.ii.springboot.specifications;

import edu.uws.ii.springboot.commands.employees.GetEmployeesCommand;
import edu.uws.ii.springboot.models.Employee;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecifications {
    public static Specification<Employee> byFilter(GetEmployeesCommand c) {
        return (root, query, cb) -> {

            if (!Long.class.equals(query.getResultType()) && !long.class.equals(query.getResultType())) {
                root.fetch("user", JoinType.LEFT);
                root.fetch("warehouses", JoinType.LEFT);
                query.distinct(true);
            }

            var p = cb.conjunction();
            if (c == null) return p;

            if (c.getId() != null) {
                p = cb.and(p, cb.equal(root.get("id"), c.getId()));
            }

            if (c.getFirstName() != null && !c.getFirstName().trim().isEmpty()) {
                String v = c.getFirstName().trim().toLowerCase();
                p = cb.and(p, cb.like(cb.lower(root.get("firstName")), v));
            }

            if (c.getLastName() != null && !c.getLastName().trim().isEmpty()) {
                String v = c.getLastName().trim().toLowerCase();
                p = cb.and(p, cb.like(cb.lower(root.get("lastName")), v));
            }

            if (c.getEmail() != null && !c.getEmail().trim().isEmpty()) {
                String v = c.getEmail().trim().toLowerCase();
                p = cb.and(p, cb.like(cb.lower(root.get("email")), v));
            }

            if (c.getPhone() != null && !c.getPhone().trim().isEmpty()) {
                String v = c.getPhone().trim().toLowerCase();
                p = cb.and(p, cb.like(cb.lower(root.get("phone")), v));
            }

            if (c.getUserName() != null && !c.getUserName().trim().isEmpty()) {
                String v = c.getUserName().trim().toLowerCase();
                var userJoin = root.join("user", JoinType.LEFT);
                p = cb.and(p, cb.like(cb.lower(userJoin.get("username")), v));
            }

            if (c.getWarehouseId() != null && c.getWarehouseId() != 0) {
                var whJoin = root.join("warehouses", JoinType.LEFT);
                p = cb.and(p, cb.equal(whJoin.get("id"), c.getWarehouseId()));
            }

            return p;
        };
    }
}
