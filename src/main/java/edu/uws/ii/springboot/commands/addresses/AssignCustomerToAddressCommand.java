package edu.uws.ii.springboot.commands.addresses;

import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Customer;
import lombok.Getter;

@Getter
public class AssignCustomerToAddressCommand {
    private Long customerId;
    private Long addressId;
    private Boolean SetAddressAsMain;

    public AssignCustomerToAddressCommand(){
        this.SetAddressAsMain = false;
    }

    public AssignCustomerToAddressCommand configureCustomer(Customer customer){
        this.customerId = customer.getId();
        return this;
    }

    public AssignCustomerToAddressCommand configureCustomer(Long customerId){
        this.customerId = customerId;
        return this;
    }

    public AssignCustomerToAddressCommand configureAddress(Long addressId){
        this.addressId = addressId;
        return this;
    }

    public AssignCustomerToAddressCommand configureAddress(Address address){
        this.addressId = address.getId();
        return this;
    }

    public AssignCustomerToAddressCommand SetAddressAsMain()
    {
        this.SetAddressAsMain = true;
        return this;
    }
}
