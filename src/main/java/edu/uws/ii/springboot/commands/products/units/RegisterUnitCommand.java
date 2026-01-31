package edu.uws.ii.springboot.commands.products.units;


import edu.uws.ii.springboot.models.Product;
import edu.uws.ii.springboot.models.Unit;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUnitCommand {
    private Unit unit;
    private Long productId;

    public RegisterUnitCommand configureUnit(Unit unit){
        this.unit = unit;
        return this;
    }

    public RegisterUnitCommand configureProduct(Long productId){
        this.productId = productId;
        return this;
    }

    public RegisterUnitCommand configureProduct(Product product){
        this.productId = product.getId();
        return this;
    }

    public RegisterUnitCommand(){
        this.unit = new Unit();
    }
}
