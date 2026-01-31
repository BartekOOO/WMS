package edu.uws.ii.springboot.services;

import edu.uws.ii.springboot.commands.warehouses.GetWarehousesCommand;
import edu.uws.ii.springboot.interfaces.IAddressesService;
import edu.uws.ii.springboot.interfaces.IWarehousesService;
import edu.uws.ii.springboot.models.Warehouse;
import edu.uws.ii.springboot.repositories.IAddressesRepository;
import edu.uws.ii.springboot.repositories.ICustomersRepository;
import edu.uws.ii.springboot.repositories.IWarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehousesService implements IWarehousesService {

    private final IWarehouseRepository warehouseRepository;
    private final IAddressesRepository addressesRepository;
    private final ICustomersRepository customersRepository;

    public WarehousesService(IWarehouseRepository warehouseRepository, IAddressesRepository addressesRepository, ICustomersRepository customersRepository) {
        this.warehouseRepository = warehouseRepository;
        this.addressesRepository = addressesRepository;
        this.customersRepository = customersRepository;
    }

    @Override
    public List<Warehouse> getWarehouses(GetWarehousesCommand command) {
        return List.of();
    }
}
