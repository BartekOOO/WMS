package edu.uws.ii.springboot.commands.products;


import edu.uws.ii.springboot.commands.addresses.RegisterAddressCommand;
import edu.uws.ii.springboot.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterProductCommand {

    public Product product;

    public RegisterProductCommand(){
        this.product = new Product();
    }

    public RegisterProductCommand (Product product){
        this.product = product;

    }

}
