package edu.uws.ii.springboot.specifications;

import edu.uws.ii.springboot.commands.employees.GetEmployeesCommand;
import edu.uws.ii.springboot.models.Employee;
import edu.uws.ii.springboot.models.Role;
import edu.uws.ii.springboot.models.User;
import edu.uws.ii.springboot.models.Warehouse;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecifications {
    public static Specification<Employee> byFilter(GetEmployeesCommand c) {
        return (root, query, cb) -> {

            boolean isCountQuery =
                    Long.class.equals(query.getResultType()) || long.class.equals(query.getResultType());

            if (!isCountQuery) {
                root.fetch("user", JoinType.LEFT);
                root.fetch("warehouses", JoinType.LEFT);
                query.distinct(true);
            }

            Predicate p = cb.conjunction();
            if (c == null) return p;

            java.util.function.Function<String, String> like = s -> "%" + s.trim().toLowerCase() + "%";

            if (c.getId() != null) {
                p = cb.and(p, cb.equal(root.get("id"), c.getId()));
            }

            if (c.getFirstName() != null && !c.getFirstName().trim().isEmpty()) {
                p = cb.and(p, cb.like(cb.lower(root.get("firstName")), like.apply(c.getFirstName())));
            }

            if (c.getLastName() != null && !c.getLastName().trim().isEmpty()) {
                p = cb.and(p, cb.like(cb.lower(root.get("lastName")), like.apply(c.getLastName())));
            }

            if (c.getEmail() != null && !c.getEmail().trim().isEmpty()) {
                p = cb.and(p, cb.like(cb.lower(root.get("email")), like.apply(c.getEmail())));
            }

            if (c.getPhone() != null && !c.getPhone().trim().isEmpty()) {
                p = cb.and(p, cb.like(cb.lower(root.get("phone")), like.apply(c.getPhone())));
            }

            Join<Employee, User> userJoin = null;

            if (c.getUserName() != null && !c.getUserName().trim().isEmpty()) {
                userJoin = root.join("user", JoinType.LEFT);
                p = cb.and(p, cb.like(cb.lower(userJoin.get("username")), like.apply(c.getUserName())));
            }

            if (c.getWarehouseId() != null && c.getWarehouseId() != 0) {
                Join<Employee, Warehouse> whJoin = root.join("warehouses", JoinType.LEFT);
                p = cb.and(p, cb.equal(whJoin.get("id"), c.getWarehouseId()));
                if (!isCountQuery) query.distinct(true);
            }

            if (c.getRole() != null) {

                if (userJoin == null) {
                    userJoin = root.join("user", JoinType.LEFT);
                }

                Join<User, Role> roleJoin = userJoin.join("roles", JoinType.LEFT);

                Predicate hasRole = cb.equal(roleJoin.get("type"), c.getRole());

                if (c.getRole() == Role.Types.ROLE_PRACOWNIK) {
                    Predicate noUser = cb.isNull(userJoin.get("id"));
                    p = cb.and(p, cb.or(noUser, hasRole));
                } else {
                    Predicate hasUser = cb.isNotNull(userJoin.get("id"));
                    p = cb.and(p, cb.and(hasUser, hasRole));
                }

                if (!isCountQuery) query.distinct(true);
            }

            return p;
        };
    }
}
