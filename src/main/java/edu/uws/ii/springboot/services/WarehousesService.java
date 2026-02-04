package edu.uws.ii.springboot.services;

import edu.uws.ii.springboot.commands.addresses.RegisterAddressCommand;
import edu.uws.ii.springboot.commands.customers.GetCustomersCommand;
import edu.uws.ii.springboot.commands.deliveries.GetDeliveriesCommand;
import edu.uws.ii.springboot.commands.sectors.DeleteSectorCommand;
import edu.uws.ii.springboot.commands.sectors.EditSectorCommand;
import edu.uws.ii.springboot.commands.sectors.GetSectorCommand;
import edu.uws.ii.springboot.commands.sectors.RegisterSectorCommand;
import edu.uws.ii.springboot.commands.warehouses.*;
import edu.uws.ii.springboot.enums.SectorTypeEnum;
import edu.uws.ii.springboot.interfaces.*;
import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Role;
import edu.uws.ii.springboot.models.Sector;
import edu.uws.ii.springboot.models.Warehouse;
import edu.uws.ii.springboot.repositories.*;
import edu.uws.ii.springboot.specifications.SectorSpecifications;
import edu.uws.ii.springboot.specifications.WarehouseSpecifications;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.sql.results.graph.entity.internal.BatchEntityInsideEmbeddableSelectFetchInitializer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.List;

@Service
public class WarehousesService implements IWarehousesService {

    private final IWarehouseRepository warehouseRepository;
    private final IAddressesRepository addressesRepository;
    private final ISectorsRepository sectorsRepository;
    private final ICustomersService customersService;
    private final IEmployeesRepository employeesRepository;
    private final IAddressesService addressesService;
    private final IDeliveriesService deliveriesService;

    public WarehousesService(IDeliveriesService deliveriesService, IEmployeesRepository employeesService, ISectorsRepository sectorsRepository, IAddressesService addressesService, IWarehouseRepository warehouseRepository, IAddressesRepository addressesRepository, ICustomersService customersService) {
        this.warehouseRepository = warehouseRepository;
        this.addressesService = addressesService;
        this.addressesRepository = addressesRepository;
        this.customersService = customersService;
        this.employeesRepository = employeesService;
        this.sectorsRepository = sectorsRepository;
        this.deliveriesService = deliveriesService;
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
        if (command == null)
            throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        var id = command.getId();
        if (id == null || id == 0)
            throw new IllegalArgumentException("Nie podano identyfikatora magazynu");

        var code = command.getCode();
        var name = command.getName();
        var description = command.getDescription();

        if (code.isBlank())
            throw new IllegalArgumentException("Nie podano kodu magazynu");

        if (name.isBlank())
            throw new IllegalArgumentException("Nie podano nazwy magazynu");

        var warehouse = warehouseRepository.findById(id).get();

        if (warehouse == null)
            new EntityNotFoundException("Magazyn o podanym identyfikatorze nie istnieje");

        var existsCmd = new GetWarehousesCommand().whereCodeEqualst(code);
        var found = this.getWarehouses(existsCmd);

        var collision = found.stream()
                .anyMatch(w -> w != null
                        && w.getId() != null
                        && !w.getId().equals(id)
                        && w.getCode() != null
                        && w.getCode().equalsIgnoreCase(code));

        if (collision)
            throw new IllegalStateException("Magazyn z takim kodem już istnieje");


        if (code != null)
            warehouse.setCode(code);

        if (name != null)
            warehouse.setName(name);

        if (description != null)
            warehouse.setDescription(description);

        warehouseRepository.save(warehouse);
    }

    @Override
    @Transactional
    public void assignEmployee(AssignEmployeeToWarehouse command) {
        if (command == null)
            throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        var employeeId = command.getEmployeeId();
        var warehouseId = command.getWarehouseId();

        if (employeeId == null || employeeId == 0)
            throw new IllegalArgumentException("Nie podano identyfikatora pracownika");

        if (warehouseId == null || warehouseId == 0)
            throw new IllegalArgumentException("Nie podano identyfikatora magazynu");

        var wOpt = warehouseRepository.findById(warehouseId);
        if (wOpt.isEmpty())
            throw new EntityNotFoundException("Magazyn o podanym identyfikatorze nie istnieje");
        var warehouse = wOpt.get();

        var eOpt = employeesRepository.findById(employeeId);
        if (eOpt.isEmpty())
            throw new EntityNotFoundException("Pracownik o podanym identyfikatorze nie istnieje");
        var employee = eOpt.get();

        boolean isEmployee = (employee.getUser() == null);

        if (!isEmployee) {
            var roles = employee.getUser().getRoles();

            isEmployee = roles != null && roles.stream()
                    .anyMatch(r -> r != null && r.getType() == Role.Types.ROLE_PRACOWNIK);
        }

        if (!isEmployee)
            throw new IllegalStateException("Nie można przypisać: pracownik ma konto bez roli PRACOWNIK.");

        for (var w : employee.getWarehouses()) {
            if (w != null && w.getId() != null && w.getId().equals(warehouseId)) {
                return;
            }
        }

        employee.getWarehouses().add(warehouse);
        employeesRepository.save(employee);
    }

    @Override
    @Transactional
    public void unassignEmployee(UnassignEmployeeFromWarehouse command) {
        if (command == null)
            throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        var employeeId = command.getEmployeeId();
        var warehouseId = command.getWarehouseId();

        if (employeeId == null || employeeId == 0)
            throw new IllegalArgumentException("Nie podano identyfikatora pracownika");

        if (warehouseId == null || warehouseId == 0)
            throw new IllegalArgumentException("Nie podano identyfikatora magazynu");

        var eOpt = employeesRepository.findById(employeeId);
        if (eOpt.isEmpty())
            throw new EntityNotFoundException("Pracownik o podanym identyfikatorze nie istnieje");
        var employee = eOpt.get();

        employee.getWarehouses().removeIf(w ->
                w != null && w.getId() != null && w.getId().equals(warehouseId)
        );

        employeesRepository.save(employee);
    }

    @Override
    @Transactional
    public void deleteWarehouse(DeleteWarehouseCommand command) {
        if (command == null)
            throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        var id = command.getId();
        if (id == null || id == 0)
            throw new IllegalArgumentException("Nie podano identyfikatora magazynu");

        var wOpt = warehouseRepository.findById(id);
        if (wOpt.isEmpty())
            throw new EntityNotFoundException("Magazyn o podanym identyfikatorze nie istnieje");

        var warehouse = wOpt.get();

        if (warehouse.getEmployees() != null) {
            for (var e : warehouse.getEmployees()) {
                if (e == null) continue;
                e.getWarehouses().removeIf(w -> w != null && w.getId() != null && w.getId().equals(id));
                employeesRepository.save(e);
            }
        }

        var containsDeliveries = !deliveriesService.getDeliveries(
                new GetDeliveriesCommand().whereWarehouseEquals(warehouse)
                        .whereQuantityGreaterThan(BigDecimal.valueOf(0))
        ).isEmpty();

        if(containsDeliveries)
            throw new IllegalStateException("Magazyn posiada stany towarowe");

        warehouse.setAddress(null);

        warehouse.setArchival(true);
        warehouseRepository.save(warehouse);

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

        var cmd = new GetSectorCommand().whereCodeEquals(code);
        var usedCode = this.getSectors(cmd).isEmpty();

        if(!usedCode)
            throw new IllegalStateException("Istnieje już sektor z takim kodem");

        sector.setWarehouse(warehouse);

        return sectorsRepository.save(sector);
    }

    @Override
    public List<Sector> getSectors(GetSectorCommand command) {
        if(command == null)
            command = new GetSectorCommand();
        return sectorsRepository.findAll(SectorSpecifications.byFilter(command));
    }

    @Override
    @Transactional
    public void deleteSector(DeleteSectorCommand command) {
        if (command == null)
            throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        var sectorId = command.getId();
        if (sectorId == null || sectorId == 0)
            throw new IllegalArgumentException("Nie podano identyfikatora sektora");

        var sector = sectorsRepository.findById(sectorId).get();

        if(sector == null)
            throw new EntityNotFoundException("Sektor z takim identyfikatorem nie istnieje");

        if(sector.getType() == SectorTypeEnum.UnloadingHub)
            throw new  IllegalStateException("Usuwanie sektora rozładunkowego jest zabronione");

        if(sector.getType() == SectorTypeEnum.LoadingHub)
            throw new  IllegalStateException("Usuwanie sektora załadunkowego jest zabronione");

        var magazineSectors = this.getSectors(new GetSectorCommand().whereWarehouseEquals(sector.getWarehouse()));
        if(magazineSectors.stream().count() == 3)
            throw new IllegalStateException("Magazyn musi posiadać conajmniej jeden zwykły sektor");

        var containsDeliveries = !deliveriesService.getDeliveries(
                new GetDeliveriesCommand().whereSectorEquals(sector)
                        .whereQuantityGreaterThan(BigDecimal.valueOf(0))
        ).isEmpty();

        if(containsDeliveries)
            throw new IllegalStateException("Sektor posiada stany towarowe");

        sectorsRepository.delete(sector);
    }

    @Override
    @Transactional
    public void editSector(EditSectorCommand command) {
        if (command == null)
            throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        var sectorId = command.getId();
        if (sectorId == null || sectorId == 0)
            throw new IllegalArgumentException("Nie podano identyfikatora sektora");

        var code = command.getCode();
        var name = command.getName();

        if(code.isBlank())
            throw new IllegalArgumentException("Sektor nie może mieć pustego kodu");

        if(name.isBlank())
            throw new IllegalArgumentException("Sektor nie może mieć pustej nazwy");

        var sectorToEdit = sectorsRepository.findById(sectorId).get();
        if(sectorToEdit == null)
            throw new EntityNotFoundException("Sektor o podanym identyfikatorze nie istnieje");

        var alreadyExists = this.getSectors(new GetSectorCommand().whereCodeEquals(code));
        if(!alreadyExists.isEmpty() && alreadyExists.getFirst().getId() != sectorId)
            throw new IllegalStateException("Istnieje już sektor z takim kodem");

        if(code != null)
            sectorToEdit.setCode(code);

        if(name != null)
            sectorToEdit.setName(name);

        sectorsRepository.save(sectorToEdit);

    }


    @Override
    public Warehouse getWarehouseDetails(Long id) {
        if (id == null || id == 0)
            throw new IllegalArgumentException("Nie podano identyfikatora magazynu");

        return this.getWarehouses(new GetWarehousesCommand().whereIdEquals(id))
                .stream()
                .findFirst()
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "Magazyn o podanym identyfikatorze nie istnieje"));
    }


}
