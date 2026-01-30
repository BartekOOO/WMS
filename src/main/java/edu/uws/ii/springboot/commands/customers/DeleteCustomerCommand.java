package edu.uws.ii.springboot.commands.customers;

import edu.uws.ii.springboot.models.Customer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteCustomerCommand {
    public Long id;

    public DeleteCustomerCommand() {

    }

    public DeleteCustomerCommand(Long id) {
        this.id = id;
    }
}
