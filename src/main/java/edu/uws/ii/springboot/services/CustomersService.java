package edu.uws.ii.springboot.services;

import edu.uws.ii.springboot.commands.addresses.AssignCustomerToAddressCommand;
import edu.uws.ii.springboot.commands.addresses.RegisterAddressCommand;
import edu.uws.ii.springboot.commands.addresses.SetAddressAsMainCommand;
import edu.uws.ii.springboot.commands.customers.DeleteCustomerCommand;
import edu.uws.ii.springboot.commands.customers.EditCustomerCommand;
import edu.uws.ii.springboot.commands.customers.GetCustomersCommand;
import edu.uws.ii.springboot.commands.customers.RegisterCustomerCommand;
import edu.uws.ii.springboot.interfaces.IAddressesService;
import edu.uws.ii.springboot.interfaces.ICustomersService;
import edu.uws.ii.springboot.models.Customer;
import edu.uws.ii.springboot.repositories.ICustomersRepository;
import edu.uws.ii.springboot.specifications.CustomerSpecifications;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomersService implements ICustomersService {

    private final ICustomersRepository customersRepository;
    private final IAddressesService addressesService;

    public CustomersService(ICustomersRepository customersRepository, IAddressesService addressesService) {
        this.customersRepository = customersRepository;
        this.addressesService = addressesService;
    }


    @Override
    @Transactional
    public Customer registerCustomer(RegisterCustomerCommand command) {
        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt");

        var acronym = command.getCustomer().getAcronym();
        var name = command.getCustomer().getFullName();
        var nip = command.getCustomer().getNip();

        if (acronym == null || acronym.isBlank())
            throw new IllegalArgumentException("Nie podano akronimu kontrahenta");

        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Nie podano nazwy kontrahenta");

        if (nip == null || nip.isBlank())
            throw new IllegalArgumentException("Nie podano nipu kontrahenta");

        var newCustomer = command.getCustomer();
        newCustomer.setMain(false);

        var testCommand = new GetCustomersCommand()
                .whereAcronymEquals(newCustomer.getAcronym())
                .whereIsNotArchival();

        var existing = this.getCustomers(testCommand).stream().findFirst().orElse(null);

        if (existing != null)
            throw new IllegalArgumentException("Kontrahent o podanym akronimie już istnieje");


        var newAddress = command.getAddress();
        var newAddressCommand = new RegisterAddressCommand();
        newAddressCommand.configureAddress(newAddress);

        var result = customersRepository.save(newCustomer);
        var result2 = addressesService.registerAddress(newAddressCommand);

        var assignCommand = new AssignCustomerToAddressCommand();
        assignCommand.configureAddress(result2);
        assignCommand.configureCustomer(result);

        addressesService.assignCustomer(assignCommand);

        var setAsMainAddress = new SetAddressAsMainCommand(result2);
        addressesService.setAddressAsMain(setAsMainAddress);

        var finalCustomer = customersRepository.getById(result.getId());

        return finalCustomer;
    }

    @Override
    @Transactional
    public void deleteCustomer(DeleteCustomerCommand command) {
        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt");
        var id = command.getId();

        if (id == null || id <= 0)
            throw new IllegalArgumentException("Błędny identyfikator kontrahenta");

        Customer customerToDelete = customersRepository.findById(id).get();
        if(customerToDelete == null)
            throw new EntityNotFoundException("Nie istnieje taki kontrahent");

        if(customerToDelete.isArchival())
            throw new EntityNotFoundException("Kontrahent został zarchiwizowany");

        if(customerToDelete.isMain())
            throw new EntityNotFoundException("Niedozwolone jest usuwanie kontrahenta reprezentującego firmę");

        customerToDelete.setArchival(true);
        customersRepository.save(customerToDelete);
    }

    @Override
    @Transactional
    public Customer editCustomer(EditCustomerCommand command) {
        if (command == null) throw new IllegalArgumentException("Przekazano pusty obiekt");

        var acronym = command.getAcronym();
        var name = command.getFullName();
        var nip = command.getNip();

        if (command.getId() == null || command.getId() <= 0)
            throw new IllegalArgumentException("Nie podano identyfikatoru kontrahenta");

        if (acronym.isBlank())
            throw new IllegalArgumentException("Nie podano akronimu kontrahenta");

        if (name.isBlank())
            throw new IllegalArgumentException("Nie podano nazwy kontrahenta");

        if (nip.isBlank())
            throw new IllegalArgumentException("Nie podano nipu kontrahenta");

        var customerToEdit = customersRepository.getById(command.getId());

        if (customerToEdit == null)
            throw new EntityNotFoundException("Kontrahent nie istnieje");

        if (customerToEdit.isArchival())
            throw new EntityNotFoundException("Kontrahent został zarchiwizowany");

        if(command.getAcronym() != null) {
            var testCommand = new GetCustomersCommand().whereAcronymEquals(command.getAcronym()).whereIsNotArchival();
            var existing = this.getCustomers(testCommand).stream().findFirst().orElse(null);
            if (existing != null && !existing.getId().equals(command.getId()))
                throw new IllegalArgumentException("Kontrahent o podanym akronimie już istnieje");

            customerToEdit.setAcronym(acronym);
        }

        if(command.getFullName() != null)
            customerToEdit.setFullName(name);

        if(command.getNip() != null)
            customerToEdit.setNip(nip);

        if(command.getCustomerType() != null)
            customerToEdit.setCustomerType(command.getCustomerType());

        if(command.getEmail() != null)
            customerToEdit.setEmail(command.getEmail());

        if(command.getUrl() != null)
            customerToEdit.setUrl(command.getUrl());

        if(command.getFoundedOn() != null)
            customerToEdit.setFoundedOn(command.getFoundedOn());

        customersRepository.save(customerToEdit);

        return customerToEdit;
    }

    @Override
    public List<Customer> getCustomers(GetCustomersCommand command) {
        if (command == null) command = new GetCustomersCommand();
        return customersRepository.findAll(CustomerSpecifications.byFilter(command));
    }
}
