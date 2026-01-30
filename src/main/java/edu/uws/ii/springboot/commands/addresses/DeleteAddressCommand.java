package edu.uws.ii.springboot.commands.addresses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteAddressCommand {
    private Long id;

    public DeleteAddressCommand() {

    }

    public DeleteAddressCommand(Long id){
        this.id = id;
    }
}
