package edu.uws.ii.springboot.specifications;

import edu.uws.ii.springboot.commands.customers.GetCustomersCommand;
import edu.uws.ii.springboot.models.Customer;
import org.springframework.data.jpa.domain.Specification;

public class CustomerSpecifications {
    public static Specification<Customer> byFilter(GetCustomersCommand c) {
        return (root, query, cb) -> {
            var p = cb.conjunction();

            if (c.getId() != null) {
                p = cb.and(p, cb.equal(root.get("id"), c.getId()));
            }

            if (c.getAcronym() != null && !c.getAcronym().isBlank()) {
                String acr = c.getAcronym().trim().toLowerCase();
                p = cb.and(p, cb.like(cb.lower(root.get("acronym")), acr + ""));
            }

            if (c.getName() != null && !c.getName().isBlank()) {
                p = cb.and(p, cb.like(cb.lower(root.get("fullName")),
                        "" + c.getName().toLowerCase() + ""));
            }
            if (c.getIsMain() != null) {
                p = cb.and(p, cb.equal(root.get("isMain"), c.getIsMain()));
            }

            if (c.getNip() != null && !c.getNip().isBlank()) {
                p = cb.and(p, cb.equal(root.get("nip"), c.getNip()));
            }

            if (c.getCustomerType() != null && c.getCustomerType().isPresent()){
                p = cb.and(p, cb.equal(root.get("customerType"), c.getCustomerType().get()));
            }

            if (c.getIsArchival() != null) {
                p = cb.and(p, cb.equal(root.get("isArchival"), c.getIsArchival()));
            }

            return p;
        };
    }
}
