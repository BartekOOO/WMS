package edu.uws.ii.springboot.interfaces;

import edu.uws.ii.springboot.commands.customers.DeleteCustomerCommand;
import edu.uws.ii.springboot.commands.customers.EditCustomerCommand;
import edu.uws.ii.springboot.commands.customers.GetCustomersCommand;
import edu.uws.ii.springboot.commands.customers.RegisterCustomerCommand;
import edu.uws.ii.springboot.models.Customer;

import java.util.List;

public interface ICustomersService {
    Customer registerCustomer(RegisterCustomerCommand command);
    void deleteCustomer(DeleteCustomerCommand command);
    Customer editCustomer(EditCustomerCommand command);
    List<Customer> getCustomers(GetCustomersCommand command);
}
