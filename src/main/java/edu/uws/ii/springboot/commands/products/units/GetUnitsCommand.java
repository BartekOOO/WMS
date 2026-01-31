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
}
