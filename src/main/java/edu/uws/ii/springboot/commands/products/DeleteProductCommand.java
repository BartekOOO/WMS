package edu.uws.ii.springboot.commands.products;

import edu.uws.ii.springboot.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteProductCommand {

    private Long id;

    public DeleteProductCommand(Product product){
        this.id = product.getId();
    }
    public DeleteProductCommand(){
    }
}
