package edu.uws.ii.springboot.commands.products.units;


import edu.uws.ii.springboot.models.Unit;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUnitCommand {
    private Unit unit;

    public RegisterUnitCommand(Unit unit){
        this.unit = unit;
    }

    public RegisterUnitCommand(){

    }
}
