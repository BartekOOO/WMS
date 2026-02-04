package edu.uws.ii.springboot.services;

import edu.uws.ii.springboot.commands.addresses.*;
import edu.uws.ii.springboot.commands.customers.GetCustomersCommand;
import edu.uws.ii.springboot.commands.warehouses.GetWarehousesCommand;
import edu.uws.ii.springboot.interfaces.IAddressesService;
import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.repositories.IAddressesRepository;
import edu.uws.ii.springboot.repositories.ICustomersRepository;
import edu.uws.ii.springboot.repositories.IWarehouseRepository;
import edu.uws.ii.springboot.specifications.AddressSpecifications;
import edu.uws.ii.springboot.specifications.CustomerSpecifications;
import edu.uws.ii.springboot.specifications.WarehouseSpecifications;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressesService implements IAddressesService {

    private final IAddressesRepository addressRepository;
    private final IWarehouseRepository  warehouseRepository;
    private final ICustomersRepository customersRepository;

    public AddressesService(IAddressesRepository addressRepository,  IWarehouseRepository warehouseRepository,   ICustomersRepository customersRepository) {
        this.addressRepository = addressRepository;
        this.customersRepository = customersRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    @Transactional
    public Address registerAddress(RegisterAddressCommand command) {
        if (command == null)
            throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        var addressToRegister = command.getAddress();

        if (addressToRegister == null)
            throw new IllegalArgumentException("Przekazano pusty obiekt");

        if (addressToRegister.getStreet() == null || addressToRegister.getStreet().isBlank())
            throw new IllegalArgumentException("Nie podano ulicy");

        if (addressToRegister.getCity() == null || addressToRegister.getCity().isBlank())
            throw new IllegalArgumentException("Nie podano miasta");

        if (addressToRegister.getZipCode() == null || addressToRegister.getZipCode().isBlank())
            throw new IllegalArgumentException("Nie podano kodu pocztowego");

        if (addressToRegister.getCountry() == null || addressToRegister.getCountry().isBlank())
            throw new IllegalArgumentException("Nie podano kraju");

        var result = addressRepository.save(addressToRegister);

        if(command.getCustomerId() != null) {
            var assignCustomerToAddress = new AssignCustomerToAddressCommand();
            assignCustomerToAddress.configureCustomer(command.getCustomerId());
            assignCustomerToAddress.configureAddress(command.getAddress());
            this.assignCustomer(assignCustomerToAddress);
        }

        var finalAddress = addressRepository.getById(result.getId());

        return finalAddress;
    }

    @Override
    @Transactional
    public void assignCustomer(AssignCustomerToAddressCommand command) {
        if (command == null)
            throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        if(command.getCustomerId() == null)
            throw new IllegalArgumentException("Nie podano identyfikatora kontrahenta");

        if(command.getAddressId() == null)
            throw new IllegalArgumentException("Nie podano identifikatora adresu");

        var customer = customersRepository.getById(command.getCustomerId());
        var address = addressRepository.getById(command.getAddressId());

        if(customer == null)
            throw new EntityNotFoundException("Kontrahent o podanym identyfikatorze nie istnieje");

        if(customer.isArchival())
            throw new EntityNotFoundException("Kontrahent został zarchiwizowany");

        if(address == null)
            throw new EntityNotFoundException("Adres o podanym identyfikatorze nie istnieje");

        if(address.isArchival())
            throw new EntityNotFoundException("Adres został zarchiwizowany");

        address.setCustomer(customer);
        addressRepository.save(address);
    }

    @Override
    @Transactional
    public void deleteAddress(DeleteAddressCommand command) {
        if (command == null)
            throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        if (command.getId() == null)
            throw new IllegalArgumentException("Nie podano identyfikatora adresu");

        var addressToDelete = addressRepository.findById(command.getId())
                .orElseThrow(() -> new EntityNotFoundException("Adres o podanym identyfikatorze nie istnieje"));

        if (addressToDelete.isArchival())
            throw new IllegalStateException("Adres już został zarchiwizowany");

        var customer = addressToDelete.getCustomer();
        if (customer == null)
            throw new IllegalStateException("Adres nie jest przypisany do kontrahenta");

        boolean hasOtherActive = false;
        for (Address addr : customer.getAddresses()) {
            if (addr == null) continue;
            if (addr.getId().equals(command.getId())) continue;
            if (addr.isArchival()) continue;
            hasOtherActive = true;
            break;
        }

        if (!hasOtherActive)
            throw new IllegalStateException("Nie można usunąć ostatniego aktywnego adresu kontrahenta");


        var warehouse = warehouseRepository.findAll(WarehouseSpecifications
                .byFilter(new GetWarehousesCommand().whereAddressIdEquals(addressToDelete.getId())));

        if(!warehouse.isEmpty())
            throw new IllegalStateException("Na ten adres zarejestrowany jest magazyn");

        if (addressToDelete.isMainAddress()) {
            for (Address addr : customer.getAddresses()) {
                if (addr == null) continue;
                if (addr.getId().equals(command.getId())) continue;
                if (addr.isArchival()) continue;

                var cmd = new SetAddressAsMainCommand(addr.getId());
                this.setAddressAsMain(cmd);
                break;
            }
        }

        addressToDelete.setArchival(true);
        addressRepository.save(addressToDelete);
    }

    @Override
    public List<Address> getAddresses(GetAddressesCommand command) {
        if (command == null) command = new GetAddressesCommand();
        return addressRepository.findAll(AddressSpecifications.byFilter(command));
    }

    @Override
    @Transactional
    public Address editAddress(EditAddressCommand command) {
        if (command == null)
            throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        if (command.getStreet().isBlank())
            throw new IllegalArgumentException("Nie podano ulicy");

        if (command.getCity().isBlank())
            throw new IllegalArgumentException("Nie podano miasta");

        if (command.getZipCode().isBlank())
            throw new IllegalArgumentException("Nie podano kodu pocztowego");

        if (command.getCountry().isBlank())
            throw new IllegalArgumentException("Nie podano kraju");

        var addressToEdit = addressRepository.getById(command.getId());

        if(addressToEdit == null)
            throw new EntityNotFoundException("Adres o podanym identyfikatorze nie istnieje");

        if(addressToEdit.isArchival())
            throw new EntityNotFoundException("Adres został zarchiwizowany");

        if(command.getStreet() != null) {
            addressToEdit.setStreet(command.getStreet());
        }

        if(command.getCity() != null) {
            addressToEdit.setCity(command.getCity());
        }

        if(command.getZipCode() != null) {
            addressToEdit.setZipCode(command.getZipCode());
        }

        if(command.getCountry() != null) {
            addressToEdit.setCountry(command.getCountry());
        }

        addressRepository.save(addressToEdit);
        return addressRepository.getById(addressToEdit.getId());
    }

    @Override
    @Transactional
    public void setAddressAsMain(SetAddressAsMainCommand command) {
        if (command == null)
            throw new IllegalArgumentException("Przekazano pusty obiekt komendy");

        if (command.getAddressId() == null)
            throw new IllegalArgumentException("Nie podano identyfikatora adresu");

        var addressTpEdit =  addressRepository.getById(command.getAddressId());

        if(addressTpEdit == null)
            throw new EntityNotFoundException("Adres o podanym identyfikatorze nie istnieje");

        if(addressTpEdit.isArchival())
            throw new EntityNotFoundException("Adres został zarchiwizowany");

        if(addressTpEdit.isMainAddress())
            return;

        var otherAddressesCommand = new GetAddressesCommand();
        otherAddressesCommand.whereCustomerEqual(addressTpEdit.getCustomer());
        List<Address> result = this.getAddresses(otherAddressesCommand);

        for(var addr : result){
            addr.setMainAddress(false);
            addressRepository.save(addr);
        }

        addressTpEdit.setMainAddress(true);
        addressRepository.save(addressTpEdit);
    }
}
