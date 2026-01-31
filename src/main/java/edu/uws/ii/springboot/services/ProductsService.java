package edu.uws.ii.springboot.services;

import edu.uws.ii.springboot.commands.addresses.AssignCustomerToAddressCommand;
import edu.uws.ii.springboot.commands.addresses.RegisterAddressCommand;
import edu.uws.ii.springboot.commands.addresses.SetAddressAsMainCommand;
import edu.uws.ii.springboot.commands.customers.GetCustomersCommand;
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
import edu.uws.ii.springboot.specifications.CustomerSpecifications;
import edu.uws.ii.springboot.specifications.ProductSpecifications;
import jakarta.transaction.Transactional;
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
        if (command == null) command = new GetProductsCommand();
        return productsRepository.findAll(ProductSpecifications.byFilter(command));
    }

    @Override
    @Transactional
    public Product registerProduct(RegisterProductCommand command) {
        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt");

        var product =command.getProduct();

        if(product == null)
            throw new IllegalArgumentException("Przekazano pusty obiekt");

        var sku = product.getSku();
        var name = product.getName();
        var unit = product.getUnit();
        var ean = product.getEan();

        if (sku == null || sku.isBlank())
            throw new IllegalArgumentException("Nie podano sku produktu");

        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Nie podano nazwy produktu");

        if (unit == null || unit.isBlank())
            throw new IllegalArgumentException("Nie podano jednostki podstawowej produktu");

        if (ean == null || ean.isBlank())
            throw new IllegalArgumentException("Nie podano ean produktu");

        var skuExistsCommand = new GetProductsCommand();
        skuExistsCommand.whereSkuEquals(sku);
        if(!(this.getProducts(skuExistsCommand).getFirst() == null))
            throw new IllegalArgumentException("Produkt o podanym sku już istnieje");

        var nameExistsCommand = new GetProductsCommand();
        nameExistsCommand.whereNameEquals(name);
        if(!(this.getProducts(nameExistsCommand).getFirst() == null))
            throw new IllegalArgumentException("Produkt o podanej nazwie już istnieje");

        var eanExistsCommand = new GetProductsCommand();
        eanExistsCommand.whereEanEquals(ean);
        if(!(this.getProducts(eanExistsCommand).getFirst() == null))
            throw new IllegalArgumentException("Produkt o podanym ean już istnieje");

        productsRepository.save(product);

        return product;
    }

    @Override
    @Transactional
    public void editProduct(EditProductCommand command) {

    }

    @Override
    @Transactional
    public void deleteProduct(DeleteProductCommand command) {

    }

    @Override
    @Transactional
    public void deleteUnit(DeleteUnitCommand command) {

    }

    @Override
    @Transactional
    public void editUnit(EditUnitCommand command) {

    }

    @Override
    @Transactional
    public Unit registerUnit(RegisterUnitCommand command) {
        return null;
    }

    @Override
    @Transactional
    public void deleteProperty(DeleteProductCommand command) {

    }

    @Override
    @Transactional
    public void editProperty(EditPropertyCommand command) {

    }

    @Override
    @Transactional
    public Property registerProperty(RegisterPropertyCommand command) {
        return null;
    }
}
