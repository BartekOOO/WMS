package edu.uws.ii.springboot.commands.products.properties;

import edu.uws.ii.springboot.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterPropertyCommand {
    private String propertyName;
    private Long productId;

    public RegisterPropertyCommand(){
    }

    public RegisterPropertyCommand configurePropertyName(String propertyName){
        this.propertyName = propertyName;
        return this;
    }

    public RegisterPropertyCommand configureProduct(Product product){
        this.productId = product.getId();
        return this;
    }

    public RegisterPropertyCommand configureProduct(Long productId){
        this.productId = productId;
        return this;
    }
}
