package edu.uws.ii.springboot.commands.customers;

import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Customer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterCustomerCommand {
    private Customer customer;
    private Address address;

    public RegisterCustomerCommand() {
        this.customer = new Customer();
        this.address = new Address();
    }

    public RegisterCustomerCommand(Customer customer, Address address) {
        this.customer = customer;
        this.address = address;
    }
}
