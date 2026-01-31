package edu.uws.ii.springboot.commands.products.units;

import edu.uws.ii.springboot.models.Unit;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteUnitCommand {
    private Long id;

    public DeleteUnitCommand(Unit  unit){
        this.id = unit.getId();
    }

    public DeleteUnitCommand(Long id){
        this.id = id;
    }

    public DeleteUnitCommand(){

    }
}
