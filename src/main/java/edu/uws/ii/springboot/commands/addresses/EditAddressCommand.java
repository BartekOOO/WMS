package edu.uws.ii.springboot.commands.addresses;

import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Customer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditAddressCommand {

    private Long id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    public EditAddressCommand() {

    }

    public EditAddressCommand(Address address) {
        this.street = address.getStreet();
        this.city = address.getCity();
        this.state = address.getState();
        this.id = address.getId();
        this.zipCode = address.getZipCode();
        this.country = address.getCountry();
    }
}
