package edu.uws.ii.springboot.commands.warehouses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteWarehouseCommand {

    private Long id;

    public DeleteWarehouseCommand(Long id) {
        this.id = id;
    }
}
