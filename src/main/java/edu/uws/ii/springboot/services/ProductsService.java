package edu.uws.ii.springboot.services;

import edu.uws.ii.springboot.commands.products.DeleteProductCommand;
import edu.uws.ii.springboot.commands.products.EditProductCommand;
import edu.uws.ii.springboot.commands.products.GetProductsCommand;
import edu.uws.ii.springboot.commands.products.RegisterProductCommand;
import edu.uws.ii.springboot.commands.products.properties.EditPropertyCommand;
import edu.uws.ii.springboot.commands.products.properties.RegisterPropertyCommand;
import edu.uws.ii.springboot.commands.products.units.DeleteUnitCommand;
import edu.uws.ii.springboot.commands.products.units.EditUnitCommand;
import edu.uws.ii.springboot.commands.products.units.RegisterUnitCommand;
import edu.uws.ii.springboot.interfaces.IProductsService;
import edu.uws.ii.springboot.models.Product;
import edu.uws.ii.springboot.models.Property;
import edu.uws.ii.springboot.models.Unit;
import edu.uws.ii.springboot.repositories.IProductsRepository;
import edu.uws.ii.springboot.repositories.IPropertiesRepository;
import edu.uws.ii.springboot.repositories.IUnitsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsService implements IProductsService {

    private final IUnitsRepository  unitsRepository;
    private final IProductsRepository productsRepository;
    private final IPropertiesRepository propertiesRepository;

    public ProductsService(IUnitsRepository unitsRepository, IProductsRepository productsRepository, IPropertiesRepository propertiesRepository) {
        this.unitsRepository = unitsRepository;
        this.productsRepository = productsRepository;
        this.propertiesRepository = propertiesRepository;
    }

    @Override
    public List<Product> getProducts(GetProductsCommand command) {
        return List.of();
    }

    @Override
    public Product registerProduct(RegisterProductCommand command) {
        return null;
    }

    @Override
    public void editProduct(EditProductCommand command) {

    }

    @Override
    public void deleteProduct(DeleteProductCommand command) {

    }

    @Override
    public void deleteUnit(DeleteUnitCommand command) {

    }

    @Override
    public void editUnit(EditUnitCommand command) {

    }

    @Override
    public Unit registerUnit(RegisterUnitCommand command) {
        return null;
    }

    @Override
    public void deleteProperty(DeleteProductCommand command) {

    }

    @Override
    public void editProperty(EditPropertyCommand command) {

    }

    @Override
    public Property registerProperty(RegisterPropertyCommand command) {
        return null;
    }
}
