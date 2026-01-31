package edu.uws.ii.springboot.interfaces;

import edu.uws.ii.springboot.commands.products.DeleteProductCommand;
import edu.uws.ii.springboot.commands.products.EditProductCommand;
import edu.uws.ii.springboot.commands.products.GetProductsCommand;
import edu.uws.ii.springboot.commands.products.RegisterProductCommand;
import edu.uws.ii.springboot.commands.products.properties.DeletePropertyCommand;
import edu.uws.ii.springboot.commands.products.properties.EditPropertyCommand;
import edu.uws.ii.springboot.commands.products.properties.RegisterPropertyCommand;
import edu.uws.ii.springboot.commands.products.units.DeleteUnitCommand;
import edu.uws.ii.springboot.commands.products.units.EditUnitCommand;
import edu.uws.ii.springboot.commands.products.units.RegisterUnitCommand;
import edu.uws.ii.springboot.models.Product;
import edu.uws.ii.springboot.models.Property;
import edu.uws.ii.springboot.models.Unit;

import java.util.List;

public interface IProductsService {
    List<Product> getProducts(GetProductsCommand command);
    Product registerProduct(RegisterProductCommand command);
    void editProduct(EditProductCommand command);
    void deleteProduct(DeleteProductCommand command);
    void deleteUnit(DeleteUnitCommand command);
    void editUnit(EditUnitCommand command);
    Unit registerUnit(RegisterUnitCommand command);
    void deleteProperty(DeletePropertyCommand command);
    void editProperty(EditPropertyCommand command);
    Property registerProperty(RegisterPropertyCommand command);
}
