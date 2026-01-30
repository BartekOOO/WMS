package edu.uws.ii.springboot.commands.addresses;

import edu.uws.ii.springboot.models.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SetAddressAsMainCommand {
    private Long addressId;

    public SetAddressAsMainCommand() {

    }

    public SetAddressAsMainCommand(Address address) {
        this.addressId = address.getId();
    }

    public SetAddressAsMainCommand(Long addressId) {
        this.addressId = addressId;
    }
}
