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
import edu.uws.ii.springboot.commands.products.properties.GetPropertiesCommand;
import edu.uws.ii.springboot.commands.products.properties.RegisterPropertyCommand;
import edu.uws.ii.springboot.commands.products.units.DeleteUnitCommand;
import edu.uws.ii.springboot.commands.products.units.EditUnitCommand;
import edu.uws.ii.springboot.commands.products.units.GetUnitsCommand;
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
import edu.uws.ii.springboot.specifications.PropertySpecifications;
import edu.uws.ii.springboot.specifications.UnitSpecifications;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        var product = command.getProduct();

        if(product == null)
            throw new IllegalArgumentException("Przekazano pusty obiekt produktu");

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

        var prod = productsRepository.save(product);

        return prod;
    }

    @Override
    @Transactional
    public void editProduct(EditProductCommand command) {

        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        var sku = command.getSku();
        var name = command.getName();
        var unit = command.getUnit();
        var ean = command.getEan();

        if (sku.isBlank())
            throw new IllegalArgumentException("Nie podano sku produktu");

        if (name.isBlank())
            throw new IllegalArgumentException("Nie podano nazwy produktu");

        if (unit.isBlank())
            throw new IllegalArgumentException("Nie podano jednostki podstawowej produktu");

        if (ean.isBlank())
            throw new IllegalArgumentException("Nie podano ean produktu");

        if(command.getId() == null || command.getId() == 0)
            throw new IllegalArgumentException("Nie przekazano identyfikatora produktu");

        var product = this.getProducts(new GetProductsCommand().whereIdEquals(command.getId())).getFirst();

        if(product == null)
            throw new EntityNotFoundException("Nie znaleziono produktu o podanym identyfikatorze");

        if(sku != null) {
            var skuExistsCommand = new GetProductsCommand();
            skuExistsCommand.whereSkuEquals(sku).whereIsNotArchival();
            var skuProduct = this.getProducts(skuExistsCommand).getFirst();
            if (skuProduct != null && skuProduct.getId() != command.getId()) {
                throw new IllegalArgumentException("Produkt o podanym sku już istnieje");
            }
            product.setSku(sku);
        }

        if(name != null) {
            var nameExistsCommand = new GetProductsCommand();
            nameExistsCommand.whereNameEquals(name).whereIsNotArchival();
            var nameProduct = this.getProducts(nameExistsCommand).getFirst();
            if (nameProduct != null && product.getId() != nameProduct.getId()) {
                throw new IllegalArgumentException("Produkt o podanej nazwie już istnieje");
            }
            product.setName(name);
        }

        if(ean != null) {
            var eanExistsCommand = new GetProductsCommand();
            eanExistsCommand.whereEanEquals(ean).whereIsNotArchival();
            var eanProduct = this.getProducts(eanExistsCommand).getFirst();
            if (eanProduct != null && product.getId() != eanProduct.getId()) {
                throw new IllegalArgumentException("Produkt o podanym ean już istnieje");
            }
            product.setEan(ean);
        }

        if(command.getBrand() != null)
            product.setBrand(command.getBrand());

        if(command.getDescription() != null)
            product.setDescription(command.getDescription());

        if(command.getUnit() != null)
            product.setUnit(command.getUnit());

        if(command.getPhotoContentType() != null)
            product.setPhotoContentType(command.getPhotoContentType());

        if(command.getPhotoContent() != null)
            product.setPhotoContent(command.getPhotoContent());

        if(command.getPhotoContent() != null)
            product.setPhotoContent(command.getPhotoContent());

        productsRepository.save(product);

    }

    @Override
    @Transactional
    public void deleteProduct(DeleteProductCommand command) {

        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        if (command.getId() == null || command.getId() == 0)
            throw new IllegalArgumentException("Nie przekazano identyfikatora produktu");

        var product = this.getProducts(new GetProductsCommand().whereIdEquals(command.getId())).getFirst();

        if (product == null)
            throw new EntityNotFoundException("Nie znaleziono produktu o podanym identyfikatorze");

        if (product.isArchival())
            throw new IllegalArgumentException("Produkt został już zarchiwizowany");

        product.setArchival(true);
        productsRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteUnit(DeleteUnitCommand command) {
        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        if(command.getId() == null || command.getId() == 0)
            throw new IllegalArgumentException("Nie przekazano identyfikatora jednostki");

        var unit = unitsRepository.getById(command.getId());

        if(unit == null)
            throw new EntityNotFoundException("Jednostka o podanym identyfikatorze nie istnieje");

        unitsRepository.delete(unit);
    }

    @Override
    @Transactional
    public void editUnit(EditUnitCommand command) {
        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        if(command.getId() == null || command.getId() == 0)
            throw new IllegalArgumentException("Nie przekazano identyfikatora właściwości");

        if(command.getUnitName().isBlank())
            throw new IllegalArgumentException("Nazwa jednostki nie może być pusta");

        if(command.getUnitMultiplier() == BigDecimal.ZERO)
            throw new IllegalArgumentException("Przelicznik jednostki nie może być zerowy");

        if(command.getUnitMultiplier().signum() < 0)
            throw new IllegalArgumentException("Przelicznik jednostki nie może być ujemny");

        var unit = unitsRepository.getById(command.getId());

        if(unit == null)
            throw new EntityNotFoundException("Jednostka o podanym identyfikatorze nie istnieje");

        if(command.getUnitName() == unit.getUnitName() && command.getUnitMultiplier() == unit.getMultiplier())
            return;

        var sameUnitCommand = new GetUnitsCommand().whereUnitNameEquals(command.getUnitName()).whereProductIdEquals(unit.getProduct().getId());
        var sameUnit = unitsRepository.findAll(UnitSpecifications.byFilter(sameUnitCommand));

        if(sameUnit != null)
            throw new IllegalArgumentException("Jednostka o takiej nazwie już istnieje");

        if(command.getUnitMultiplier() != null)
            unit.setMultiplier(command.getUnitMultiplier());

        if(command.getUnitName() != null)
            unit.setUnitName(command.getUnitName());

        unitsRepository.save(unit);
    }

    @Override
    @Transactional
    public Unit registerUnit(RegisterUnitCommand command) {
        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        if(command.getUnit() == null)
            throw new IllegalArgumentException("Przekazano pusty obiekt jednostki");

        var unitName = command.getUnit().getUnitName();
        var multiplierUnit = command.getUnit().getMultiplier();
        var productId = command.getUnit().getProduct().getId();

        if(unitName == null || unitName.isBlank())
            throw new IllegalArgumentException("Nie podano nazwy jednostki");

        if(productId == null || productId == 0)
            throw new IllegalArgumentException("Nie podano identyfikatora produktu");

        var product =  this.getProducts(new GetProductsCommand().whereIdEquals(productId)).getFirst();
        if(product == null)
            throw new EntityNotFoundException("Produkt o podanym identyfikatorze nie istnieje");

        var sameUnitCommand = new GetUnitsCommand().whereUnitNameEquals(unitName).whereProductIdEquals(productId);
        var sameUnit = unitsRepository.findAll(UnitSpecifications.byFilter(sameUnitCommand));

        if(sameUnit != null)
            throw new IllegalArgumentException("Jednostka już istnieje");

        var newUnit = command.getUnit();
        newUnit.setProduct(product);

        return  unitsRepository.save(newUnit);
    }

    @Override
    @Transactional
    public void deleteProperty(DeleteProductCommand command) {
        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        if(command.getId() == null || command.getId() == 0)
            throw new IllegalArgumentException("Nie przekazano identyfikatora właściwości");

        var property = propertiesRepository.getById(command.getId());

        if(property == null)
            throw new EntityNotFoundException("Właściwość o podanym identyfikatorze nie istnieje");

        propertiesRepository.delete(property);
    }

    @Override
    @Transactional
    public void editProperty(EditPropertyCommand command) {
        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        if(command.getId() == null || command.getId() == 0)
            throw new IllegalArgumentException("Nie przekazano identyfikatora właściwości");

        if(command.getPropertyName().isBlank())
            throw new IllegalArgumentException("Nazwa właściwości nie może być pusta");

        var property = propertiesRepository.getById(command.getId());

        if(property == null)
            throw new EntityNotFoundException("Właściwość o podanym identyfikatorze nie istnieje");

        if(command.getPropertyName() == property.getPropertyName() || command.getPropertyName() == null)
            return;

        var samePropertyCommand = new GetPropertiesCommand().wherePropertyEquals(command.getPropertyName());
        var sameProperty = propertiesRepository.findAll(PropertySpecifications.byFilter(samePropertyCommand));

        if(sameProperty != null)
            throw new IllegalArgumentException("Cecha o takiej nazwie już istnieje");

        property.setPropertyName(command.getPropertyName());

        propertiesRepository.save(property);
    }

    @Override
    @Transactional
    public Property registerProperty(RegisterPropertyCommand command) {
        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        var property = command.getPropertyName();
        var productId = command.getProductId();

        if(property == null || property.isBlank())
            throw new IllegalArgumentException("Nie podano nazwy cechy");

        if(productId == null || productId == 0)
            throw new IllegalArgumentException("Nie podano identyfikatora produktu");

        var product =  this.getProducts(new GetProductsCommand().whereIdEquals(productId)).getFirst();
        if(product == null)
            throw new EntityNotFoundException("Produkt o podanym identyfikatorze nie istnieje");

        var samePropertyCommand = new GetPropertiesCommand().wherePropertyEquals(property).whereProductId(productId);
        var sameProperty = propertiesRepository.findAll(PropertySpecifications.byFilter(samePropertyCommand));

        if(sameProperty != null)
            throw new IllegalArgumentException("Cecha już istnieje");

        var newProperty = new Property();
        newProperty.setPropertyName(property);
        newProperty.setProduct(product);

        var prop = propertiesRepository.save(newProperty);
        return prop;
    }
}
