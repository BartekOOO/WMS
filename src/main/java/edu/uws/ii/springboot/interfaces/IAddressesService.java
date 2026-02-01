package edu.uws.ii.springboot.interfaces;

import edu.uws.ii.springboot.commands.addresses.*;
import edu.uws.ii.springboot.models.Address;

import java.util.List;

public interface IAddressesService {
    Address registerAddress(RegisterAddressCommand command);
    void assignCustomer(AssignCustomerToAddressCommand command);
    void deleteAddress(DeleteAddressCommand command);
    List<Address> getAddresses(GetAddressesCommand command);
    Address editAddress(EditAddressCommand command);
    void setAddressAsMain(SetAddressAsMainCommand command);
}
