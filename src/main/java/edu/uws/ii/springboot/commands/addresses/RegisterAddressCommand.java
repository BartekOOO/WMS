package edu.uws.ii.springboot.commands.addresses;

import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Customer;
import edu.uws.ii.springboot.models.Warehouse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterAddressCommand {
    private Address address;
    private Long customerId;

    public RegisterAddressCommand() {
        this.address = new Address();
    }

    public RegisterAddressCommand configureAddress(Address address) {
        this.address = address;
        return this;
    }

    public RegisterAddressCommand configureCustomer(long customerId){
        this.customerId = customerId;
        return this;
    }


    public RegisterAddressCommand configureCustomer(Customer customer){
        this.customerId = customer.getId();
        return this;
    }

}
