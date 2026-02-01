package edu.uws.ii.springboot.services;

import edu.uws.ii.springboot.commands.addresses.RegisterAddressCommand;
import edu.uws.ii.springboot.commands.customers.GetCustomersCommand;
import edu.uws.ii.springboot.commands.sectors.GetSectorsCommand;
import edu.uws.ii.springboot.commands.sectors.RegisterSectorCommand;
import edu.uws.ii.springboot.commands.warehouses.*;
import edu.uws.ii.springboot.enums.SectorTypeEnum;
import edu.uws.ii.springboot.interfaces.IAddressesService;
import edu.uws.ii.springboot.interfaces.ICustomersService;
import edu.uws.ii.springboot.interfaces.IWarehousesService;
import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Sector;
import edu.uws.ii.springboot.models.Warehouse;
import edu.uws.ii.springboot.repositories.IAddressesRepository;
import edu.uws.ii.springboot.repositories.ICustomersRepository;
import edu.uws.ii.springboot.repositories.ISectorsRepository;
import edu.uws.ii.springboot.repositories.IWarehouseRepository;
import edu.uws.ii.springboot.specifications.SectorSpecifications;
import edu.uws.ii.springboot.specifications.WarehouseSpecifications;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.sql.results.graph.entity.internal.BatchEntityInsideEmbeddableSelectFetchInitializer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehousesService implements IWarehousesService {

    private final IWarehouseRepository warehouseRepository;
    private final IAddressesRepository addressesRepository;
    private final ICustomersRepository customersRepository;
    private final ISectorsRepository sectorsRepository;
    private final ICustomersService  customersService;
    private final IAddressesService addressesService;

    public WarehousesService(ICustomersService customersService, ISectorsRepository sectorsRepository, IAddressesService addressesService, IWarehouseRepository warehouseRepository, IAddressesRepository addressesRepository, ICustomersRepository customersRepository) {
        this.warehouseRepository = warehouseRepository;
        this.addressesService = addressesService;
        this.addressesRepository = addressesRepository;
        this.customersRepository = customersRepository;
        this.customersService = customersService;
        this.sectorsRepository = sectorsRepository;
    }

    @Override
    public List<Warehouse> getWarehouses(GetWarehousesCommand command) {
        if(command == null)
            command = new  GetWarehousesCommand();
        return warehouseRepository.findAll(WarehouseSpecifications.byFilter(command));
    }

    @Override
    @Transactional
    public Warehouse registerWarehouse(RegisterWarehouseCommand command) {
        if (command == null)
            throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        var warehouse = command.getWarehouse();

        if(warehouse == null)
            throw new  IllegalArgumentException("Przekazano pusty obiekt magazynu");

        if(command.getSector() == null || command.getLoadingSector() == null || command.getUnloadingSector() == null)
            throw new  IllegalArgumentException("Nie przekazano danych odnośnie sektorów magazynu");

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
        if(command.getAddressId() != null && command.getAddressId() != 0){
            var customerAddress = addressesRepository.getById(command.getAddressId());
            if(customerAddress == null)
                throw new EntityNotFoundException("Nie istnieje adres o takim identyfikatorze");

            if(!customerAddress.getCustomer().isMain())
                throw new IllegalArgumentException("Ten adres nie należy do kontrahenta reprezentujacego firmę");

            if(!(this.getWarehouses(new GetWarehousesCommand().whereAddressIdEquals(command.getAddressId()))).isEmpty())
                throw new IllegalArgumentException("Do tego adresu został już przypisany magazyn");

            address = customerAddress;
        } else {
            var newAddressCommand = new RegisterAddressCommand();
            newAddressCommand.configureAddress(command.getAddress());
            var mainCustomer = customersService.getCustomers(new GetCustomersCommand().whereIsMain(true)).getFirst();
            newAddressCommand.configureCustomer(mainCustomer);
            address = addressesService.registerAddress(newAddressCommand);
        }


        warehouse.setAddress(address);
        warehouseRepository.save(warehouse);

        var loadingSector = this.registerSector(new  RegisterSectorCommand().configureSector(command.getLoadingSector()).configureWarehouse(warehouse));
        var unloadingSector = this.registerSector(new RegisterSectorCommand().configureSector(command.getUnloadingSector()).configureWarehouse(warehouse));
        var sector = this.registerSector(new RegisterSectorCommand().configureSector(command.getSector()).configureWarehouse(warehouse));

        warehouse.addSector(sector);
        warehouse.addSector(loadingSector);
        warehouse.addSector(unloadingSector);

        return warehouseRepository.save(warehouse);
    }

    @Override
    @Transactional
    public void editWarehouse(EditWarehouseCommand command) {

    }

    @Override
    @Transactional
    public void assignEmployee(AssignEmployeeToWarehouse command) {

    }

    @Override
    @Transactional
    public void unassignEmployee(UnassignEmployeeFromWarehouse command) {

    }

    @Override
    @Transactional
    public Sector registerSector(RegisterSectorCommand command) {
        if (command == null)
            throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        var sector = command.getSector();
        if (sector == null)
            throw new IllegalArgumentException("Przekazano pusty obiekt sektora");

        if (command.getWarehouseId() == null)
            throw new IllegalArgumentException("Nie podano identyfikatora magazynu");

        var type = sector.getType();
        var name = sector.getName();
        var code = sector.getCode();

        if (type == null)
            throw new IllegalArgumentException("Nie podano typu sektora");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Nie podano nazwy sektora");
        if (code == null || code.isBlank())
            throw new IllegalArgumentException("Nie podano kodu sektora");

        var warehouse = warehouseRepository.getById(command.getWarehouseId());

        if (type == SectorTypeEnum.LoadingHub && warehouse.getLoadingHub() != null)
            throw new IllegalStateException("Magazyn '" + warehouse.getCode() + "' już posiada sektor załadunkowy");

        if (type == SectorTypeEnum.UnloadingHub && warehouse.getUnloadingHub() != null)
            throw new IllegalStateException("Magazyn '" + warehouse.getCode() + "' już posiada sektor rozładunkowy");

        var cmd = new GetSectorsCommand().whereCodeEquals(code);
        var usedCode = this.getSectors(cmd).isEmpty();

        if(!usedCode)
            throw new IllegalStateException("Istnieje już sektor z takim kodem");

        sector.setWarehouse(warehouse);

        return sectorsRepository.save(sector);
    }

    @Override
    public List<Sector> getSectors(GetSectorsCommand command) {
        if(command == null)
            command = new GetSectorsCommand();
        return sectorsRepository.findAll(SectorSpecifications.byFilter(command));
    }

}
