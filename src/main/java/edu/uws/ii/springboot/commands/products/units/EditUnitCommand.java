package edu.uws.ii.springboot.commands.products.units;

import edu.uws.ii.springboot.models.Unit;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EditUnitCommand {
    private Long id;
    private String unitName;
    private BigDecimal unitMultiplier;

    public EditUnitCommand(Unit unit){
        unitName = unit.getUnitName();
        unitMultiplier = unit.getMultiplier();
        id = unit.getId();
    }

    public EditUnitCommand(){

    }
}
