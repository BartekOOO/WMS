package edu.uws.ii.springboot.specifications;

import edu.uws.ii.springboot.commands.addresses.GetAddressesCommand;
import edu.uws.ii.springboot.models.Address;
import org.springframework.data.jpa.domain.Specification;

public class AddressSpecifications {

    public static Specification<Address> byFilter(GetAddressesCommand c) {
        return (root, query, cb) -> {
            var p = cb.conjunction();

            if (c == null) return p;

            if (c.getId() != null) {
                p = cb.and(p, cb.equal(root.get("id"), c.getId()));
            }

            if (c.getCustomerId() != null) {
                p = cb.and(p, cb.equal(root.get("customer").get("id"), c.getCustomerId()));
            }

            if (c.getWarehouseId() != null) {
                p = cb.and(p, cb.equal(root.get("warehouse").get("id"), c.getWarehouseId()));
            }

            if (c.getIsMain() != null) {
                p = cb.and(p, cb.equal(root.get("isMainAddress"), c.getIsMain()));
            }

            return p;
        };
    }
}
