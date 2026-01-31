package edu.uws.ii.springboot.commands.products.units;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GetUnitsCommand {
    private Long id;
    private BigDecimal multiplier;
    private String unitName;
    private Long productId;

    public GetUnitsCommand whereIdEquals(Long id)
    {
        this.id=id;
        return this;
    }

    public GetUnitsCommand whereMultiplierEquals(BigDecimal multiplier) {
        this.multiplier=multiplier;
        return this;
    }

    public GetUnitsCommand whereUnitNameEquals(String unitName) {
        this.unitName=unitName;
        return this;
    }

    public GetUnitsCommand whereProductIdEquals(Long productId) {
        this.productId=productId;
        return this;
    }
}
