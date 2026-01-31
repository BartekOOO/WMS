package edu.uws.ii.springboot.services;

import edu.uws.ii.springboot.commands.products.DeleteProductCommand;
import edu.uws.ii.springboot.commands.products.EditProductCommand;
import edu.uws.ii.springboot.commands.products.GetProductsCommand;
import edu.uws.ii.springboot.commands.products.RegisterProductCommand;
import edu.uws.ii.springboot.commands.products.properties.DeletePropertyCommand;
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
import edu.uws.ii.springboot.specifications.ProductSpecifications;
import edu.uws.ii.springboot.specifications.PropertySpecifications;
import edu.uws.ii.springboot.specifications.UnitSpecifications;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ProductsService implements IProductsService {

    private final IUnitsRepository unitsRepository;
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
        if (product == null) throw new IllegalArgumentException("Przekazano pusty obiekt produktu");

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

        var skuExists = !this.getProducts(new GetProductsCommand().whereSkuEquals(sku)).isEmpty();
        if (skuExists) throw new IllegalArgumentException("Produkt o podanym sku już istnieje");

        var nameExists = !this.getProducts(new GetProductsCommand().whereNameEquals(name)).isEmpty();
        if (nameExists) throw new IllegalArgumentException("Produkt o podanej nazwie już istnieje");

        var eanExists = !this.getProducts(new GetProductsCommand().whereEanEquals(ean)).isEmpty();
        if (eanExists) throw new IllegalArgumentException("Produkt o podanym ean już istnieje");

        product.setCreatedAt(LocalDateTime.now());

        return productsRepository.save(product);
    }

    @Override
    @Transactional
    public void editProduct(EditProductCommand command) {
        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        if (command.getId() == null || command.getId() == 0)
            throw new IllegalArgumentException("Nie przekazano identyfikatora produktu");

        var sku = command.getSku();
        var name = command.getName();
        var unit = command.getUnit();
        var ean = command.getEan();

        if (sku != null && sku.isBlank())
            throw new IllegalArgumentException("Nie podano sku produktu");
        if (name != null && name.isBlank())
            throw new IllegalArgumentException("Nie podano nazwy produktu");
        if (unit != null && unit.isBlank())
            throw new IllegalArgumentException("Nie podano jednostki podstawowej produktu");
        if (ean != null && ean.isBlank())
            throw new IllegalArgumentException("Nie podano ean produktu");

        var list = this.getProducts(new GetProductsCommand().whereIdEquals(command.getId()));
        var product = list.isEmpty() ? null : list.get(0);

        if (product == null)
            throw new EntityNotFoundException("Nie znaleziono produktu o podanym identyfikatorze");

        if(product.isArchival())
            throw new EntityNotFoundException("Produkt został zarchiwizowany");

        if (sku != null) {
            var skuDup = this.getProducts(new GetProductsCommand().whereSkuEquals(sku).whereIsNotArchival())
                    .stream()
                    .filter(p -> !Objects.equals(p.getId(), command.getId()))
                    .findFirst()
                    .orElse(null);

            if (skuDup != null)
                throw new IllegalArgumentException("Produkt o podanym sku już istnieje");

            if (command.getPhotoName() != null)
                product.setPhotoName(command.getPhotoName());

            if (command.getPhotoContentType() != null)
                product.setPhotoContentType(command.getPhotoContentType());

            if (command.getPhotoContent() != null)
                product.setPhotoContent(command.getPhotoContent());


            product.setSku(sku);
        }

        if (name != null) {
            var nameDup = this.getProducts(new GetProductsCommand().whereNameEquals(name).whereIsNotArchival())
                    .stream()
                    .filter(p -> !Objects.equals(p.getId(), command.getId()))
                    .findFirst()
                    .orElse(null);

            if (nameDup != null)
                throw new IllegalArgumentException("Produkt o podanej nazwie już istnieje");

            product.setName(name);
        }

        if (ean != null) {
            var eanDup = this.getProducts(new GetProductsCommand().whereEanEquals(ean).whereIsNotArchival())
                    .stream()
                    .filter(p -> !Objects.equals(p.getId(), command.getId()))
                    .findFirst()
                    .orElse(null);

            if (eanDup != null)
                throw new IllegalArgumentException("Produkt o podanym ean już istnieje");

            product.setEan(ean);
        }

        if (command.getBrand() != null)
            product.setBrand(command.getBrand());

        if (command.getDescription() != null)
            product.setDescription(command.getDescription());

        if (command.getUnit() != null)
            product.setUnit(command.getUnit());

        if (command.getPhotoContentType() != null)
            product.setPhotoContentType(command.getPhotoContentType());

        if (command.getPhotoContent() != null)
            product.setPhotoContent(command.getPhotoContent());

        productsRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(DeleteProductCommand command) {
        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt komendy");
        if (command.getId() == null || command.getId() == 0)
            throw new IllegalArgumentException("Nie przekazano identyfikatora produktu");

        var list = this.getProducts(new GetProductsCommand().whereIdEquals(command.getId()));
        var product = list.isEmpty() ? null : list.get(0);

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
        if (command.getId() == null || command.getId() == 0)
            throw new IllegalArgumentException("Nie przekazano identyfikatora jednostki");

        var unit = unitsRepository.getById(command.getId());
        if (unit == null)
            throw new EntityNotFoundException("Jednostka o podanym identyfikatorze nie istnieje");

        if(unit.getProduct().isArchival())
            throw new EntityNotFoundException("Operacja niemożliwa. Produkt został zarchiwizowany");

        unitsRepository.delete(unit);
    }

    @Override
    @Transactional
    public void editUnit(EditUnitCommand command) {
        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        if (command.getId() == null || command.getId() == 0)
            throw new IllegalArgumentException("Nie przekazano identyfikatora jednostki");

        if (command.getUnitName() != null && command.getUnitName().isBlank())
            throw new IllegalArgumentException("Nazwa jednostki nie może być pusta");

        if (command.getUnitMultiplier() != null) {
            if (command.getUnitMultiplier().compareTo(BigDecimal.ZERO) == 0)
                throw new IllegalArgumentException("Przelicznik jednostki nie może być zerowy");
            if (command.getUnitMultiplier().signum() < 0)
                throw new IllegalArgumentException("Przelicznik jednostki nie może być ujemny");
        }

        var unit = unitsRepository.getById(command.getId());
        if (unit == null)
            throw new EntityNotFoundException("Jednostka o podanym identyfikatorze nie istnieje");

        if(unit.getProduct().isArchival())
            throw new EntityNotFoundException("Operacja niemożliwa. Produkt został zarchiwizowany");

        boolean nameUnchanged = (command.getUnitName() == null)
                || Objects.equals(command.getUnitName(), unit.getUnitName());

        boolean multiplierUnchanged = (command.getUnitMultiplier() == null)
                || (unit.getMultiplier() != null && command.getUnitMultiplier().compareTo(unit.getMultiplier()) == 0);

        if (nameUnchanged && multiplierUnchanged)
            return;

        if (command.getUnitName() != null) {
            var sameUnitCommand = new GetUnitsCommand()
                    .whereUnitNameEquals(command.getUnitName())
                    .whereProductIdEquals(unit.getProduct().getId());

            var sameUnits = unitsRepository.findAll(UnitSpecifications.byFilter(sameUnitCommand));

            boolean duplicateExists = sameUnits.stream()
                    .anyMatch(u -> !Objects.equals(u.getId(), unit.getId()));

            if (duplicateExists)
                throw new IllegalArgumentException("Jednostka o takiej nazwie już istnieje");
        }

        if (command.getUnitMultiplier() != null)
            unit.setMultiplier(command.getUnitMultiplier());

        if (command.getUnitName() != null)
            unit.setUnitName(command.getUnitName());

        unitsRepository.save(unit);
    }

    @Override
    @Transactional
    public Unit registerUnit(RegisterUnitCommand command) {
        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt komendy");
        if (command.getUnit() == null) throw new IllegalArgumentException("Przekazano pusty obiekt jednostki");

        var unitName = command.getUnit().getUnitName();
        var multiplier = command.getUnit().getMultiplier();

        var productObj = command.getUnit().getProduct();
        var productId = (productObj == null) ? null : productObj.getId();

        if (unitName == null || unitName.isBlank())
            throw new IllegalArgumentException("Nie podano nazwy jednostki");

        if (multiplier == null)
            throw new IllegalArgumentException("Nie podano przelicznika jednostki");
        if (multiplier.compareTo(BigDecimal.ZERO) == 0)
            throw new IllegalArgumentException("Przelicznik jednostki nie może być zerowy");
        if (multiplier.signum() < 0)
            throw new IllegalArgumentException("Przelicznik jednostki nie może być ujemny");

        if (productId == null || productId == 0)
            throw new IllegalArgumentException("Nie podano identyfikatora produktu");

        var prodList = this.getProducts(new GetProductsCommand().whereIdEquals(productId));
        var product = prodList.isEmpty() ? null : prodList.get(0);

        if (product == null)
            throw new EntityNotFoundException("Produkt o podanym identyfikatorze nie istnieje");

        if(product.isArchival())
            throw new EntityNotFoundException("Operacja niemożliwa. Produkt został zarchiwizowany");

        var sameUnitCommand = new GetUnitsCommand()
                .whereUnitNameEquals(unitName)
                .whereProductIdEquals(productId);

        var sameUnits = unitsRepository.findAll(UnitSpecifications.byFilter(sameUnitCommand));

        if (!sameUnits.isEmpty())
            throw new IllegalArgumentException("Jednostka już istnieje");

        var newUnit = command.getUnit();
        newUnit.setProduct(product);

        return unitsRepository.save(newUnit);
    }

    @Override
    @Transactional
    public void deleteProperty(DeletePropertyCommand command) {
        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt komendy");
        if (command.getId() == null || command.getId() == 0)
            throw new IllegalArgumentException("Nie przekazano identyfikatora właściwości");

        var property = propertiesRepository.getById(command.getId());
        if (property == null)
            throw new EntityNotFoundException("Właściwość o podanym identyfikatorze nie istnieje");

        if(property.getProduct().isArchival())
            throw new EntityNotFoundException("Operacja niemożliwa. Produkt został zarchiwizowany");

        propertiesRepository.delete(property);
    }

    @Override
    @Transactional
    public void editProperty(EditPropertyCommand command) {
        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        if (command.getId() == null || command.getId() == 0)
            throw new IllegalArgumentException("Nie przekazano identyfikatora właściwości");

        if (command.getPropertyName() != null && command.getPropertyName().isBlank())
            throw new IllegalArgumentException("Nazwa właściwości nie może być pusta");

        var property = propertiesRepository.getById(command.getId());
        if (property == null)
            throw new EntityNotFoundException("Właściwość o podanym identyfikatorze nie istnieje");

        if (command.getPropertyName() == null)
            return;

        if (Objects.equals(command.getPropertyName(), property.getPropertyName()))
            return;

        var productId = property.getProduct() != null ? property.getProduct().getId() : null;

        var product = productsRepository.getById(productId);

        if(product.isArchival())
            throw new EntityNotFoundException("Operacja niemożliwa. Produkt został zarchiwizowany");

        var samePropertyCommand = new GetPropertiesCommand()
                .wherePropertyEquals(command.getPropertyName());

        if (productId != null) {
            samePropertyCommand.whereProductId(productId);
        }

        var sameProperties = propertiesRepository.findAll(PropertySpecifications.byFilter(samePropertyCommand));

        boolean duplicateExists = sameProperties.stream()
                .anyMatch(p -> !Objects.equals(p.getId(), property.getId()));

        if (duplicateExists)
            throw new IllegalArgumentException("Cecha o takiej nazwie już istnieje");

        property.setPropertyName(command.getPropertyName());
        propertiesRepository.save(property);
    }

    @Override
    @Transactional
    public Property registerProperty(RegisterPropertyCommand command) {
        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        var propertyName = command.getPropertyName();
        var productId = command.getProductId();

        if (propertyName == null || propertyName.isBlank())
            throw new IllegalArgumentException("Nie podano nazwy cechy");

        if (productId == null || productId == 0)
            throw new IllegalArgumentException("Nie podano identyfikatora produktu");

        var prodList = this.getProducts(new GetProductsCommand().whereIdEquals(productId));
        var product = prodList.isEmpty() ? null : prodList.get(0);

        if (product == null)
            throw new EntityNotFoundException("Produkt o podanym identyfikatorze nie istnieje");

        if(product.isArchival())
            throw new EntityNotFoundException("Operacja niemożliwa. Produkt został zarchiwizowany");

        var samePropertyCommand = new GetPropertiesCommand()
                .wherePropertyEquals(propertyName)
                .whereProductId(productId);

        var sameProperties = propertiesRepository.findAll(PropertySpecifications.byFilter(samePropertyCommand));

        if (!sameProperties.isEmpty())
            throw new IllegalArgumentException("Cecha już istnieje");

        var newProperty = new Property();
        newProperty.setPropertyName(propertyName);
        newProperty.setProduct(product);

        return propertiesRepository.save(newProperty);
    }
}
