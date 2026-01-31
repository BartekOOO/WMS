package edu.uws.ii.springboot.services;

import edu.uws.ii.springboot.commands.addresses.RegisterAddressCommand;
import edu.uws.ii.springboot.commands.customers.GetCustomersCommand;
import edu.uws.ii.springboot.commands.warehouses.*;
import edu.uws.ii.springboot.interfaces.IAddressesService;
import edu.uws.ii.springboot.interfaces.IWarehousesService;
import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Warehouse;
import edu.uws.ii.springboot.repositories.IAddressesRepository;
import edu.uws.ii.springboot.repositories.ICustomersRepository;
import edu.uws.ii.springboot.repositories.IWarehouseRepository;
import edu.uws.ii.springboot.specifications.WarehouseSpecifications;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehousesService implements IWarehousesService {

    private final IWarehouseRepository warehouseRepository;
    private final IAddressesRepository addressesRepository;
    private final ICustomersRepository customersRepository;
    private final IAddressesService addressesService;

    public WarehousesService(IAddressesService addressesService, IWarehouseRepository warehouseRepository, IAddressesRepository addressesRepository, ICustomersRepository customersRepository) {
        this.warehouseRepository = warehouseRepository;
        this.addressesService = addressesService;
        this.addressesRepository = addressesRepository;
        this.customersRepository = customersRepository;
    }

    @Override
    public List<Warehouse> getWarehouses(GetWarehousesCommand command) {
        if(command == null)
            command = new  GetWarehousesCommand();
        return warehouseRepository.findAll(WarehouseSpecifications.byFilter(command));
    }

    @Override
    public Warehouse registerWarehouse(RegisterWarehouseCommand command) {
        if (command == null)
            throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        var warehouse = command.getWarehouse();

        if(warehouse == null)
            throw new  IllegalArgumentException("Przekazano pusty obiekt magazynu");

        var code = warehouse.getCode();
        var name = warehouse.getName();

        if (code == null || code.isBlank())
            throw new IllegalArgumentException("Nie podano kodu magazynu");

        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Nie podano nazwy magazynu");

        var magazineExistsCommand = new GetWarehousesCommand();
        magazineExistsCommand.whereCodeEqualst(code);

        if(!this.getWarehouses(magazineExistsCommand).isEmpty())
            throw new IllegalStateException("Magazyn z takim kodem już istnieje");

        if(command.getAddress() == null && (command.getAddressId() == null || command.getAddressId() == 0))
            throw new IllegalArgumentException("Nie udało się powiązać adresu z magazynem");

        var address = new Address();
        if(command.getAddress() != null){
            var newAddressCommand = new RegisterAddressCommand();
            newAddressCommand.configureAddress(command.getAddress());
            address = addressesService.registerAddress(newAddressCommand);
        } else {
            var customerAddress = addressesRepository.getById(command.getAddressId());
            if(customerAddress == null)
                throw new EntityNotFoundException("Nie istnieje adres o takim identyfikatorze");

            if(!customerAddress.getCustomer().isMain())
                throw new IllegalArgumentException("Ten adres nie należy do kontrahenta reprezentujacego firmę");

            if(customerAddress.getWarehouse() != null)
                throw new IllegalArgumentException("Do adresu '" + customerAddress.getStreet() + "' został już przypisane magazyn");

            address = customerAddress;
        }

        warehouse.setAddress(address);

    }

    @Override
    public void editWarehouse(EditWarehouseCommand command) {

    }

    @Override
    public void assignEmployee(AssignEmployeeToWarehouse command) {

    }

    @Override
    public void unassignEmployee(UnassignEmployeeFromWarehouse command) {

    }
}
