package edu.uws.ii.springboot.controllers;

import edu.uws.ii.springboot.commands.addresses.*;
import edu.uws.ii.springboot.commands.customers.DeleteCustomerCommand;
import edu.uws.ii.springboot.commands.customers.EditCustomerCommand;
import edu.uws.ii.springboot.commands.customers.GetCustomersCommand;
import edu.uws.ii.springboot.commands.customers.RegisterCustomerCommand;
import edu.uws.ii.springboot.interfaces.IAddressesService;
import edu.uws.ii.springboot.interfaces.ICustomersService;
import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/Addresses")
public class AddressesController {
    private final IAddressesService addressesService;
    private  final ICustomersService customersService;

    public AddressesController(IAddressesService addressesService,  ICustomersService customersService) {
        this.addressesService = addressesService;
        this.customersService = customersService;
    }

    @GetMapping("/AddForm")
    public String AddForm(Model model, Long customerId){
        model.addAttribute("content", "addresses/addForm :: fragment");
        var command = new RegisterAddressCommand();
        command.setCustomerId(customerId);
        command.setAddress(new Address());

        var customerCommand = new GetCustomersCommand().whereIdEquals(customerId);
        var customer = customersService.getCustomers(customerCommand).getFirst();

        if(customer == null){
            model.addAttribute("error", "Nie udało się pobrać danych adresu");
        }

        model.addAttribute("customer", customer);
        model.addAttribute("address", command);
        model.addAttribute("title", "WMS • Dodawanie adresu");
        return "app";
    }

    @GetMapping("/EditForm")
    public String EditForm(Model model, @RequestParam(required = false) Long id){
        var command = new GetAddressesCommand();
        command.whereIdEqual(id);
        var addressToEdit = addressesService.getAddresses(command).getFirst();

        model.addAttribute("content", "addresses/editForm :: fragment");
        model.addAttribute("command", new EditAddressCommand(addressToEdit));
        model.addAttribute("customer", addressToEdit.getCustomer());
        model.addAttribute("address", addressToEdit);
        model.addAttribute("title", "WMS • Edytowanie kontrahenta");
        return "app";
    }

    @PostMapping("RegisterAddress")
    public String Register(@ModelAttribute RegisterAddressCommand command, Model model, RedirectAttributes ra ){
        try {
            var result = addressesService.registerAddress(command);
            ra.addFlashAttribute("success", "Pomyślnie udało się dodać nowy adres");
            return "redirect:/Addresses/EditForm?id=" + result.getId();
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("content", "addresses/addForm :: fragment");
            model.addAttribute("command", command);
            model.addAttribute("title", "WMS • Dodawanie adresu");
            return "app";
        }
    }

    @PostMapping("EditAddress")
    public String Edit(@ModelAttribute EditAddressCommand command, Model model, RedirectAttributes ra ){
        try {
            var result = addressesService.editAddress(command);
            ra.addFlashAttribute("success", "Pomyślnie udało się zapisać adres");
            return "redirect:/Addresses/EditForm?id=" + result.getId();
        } catch (Exception ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/Addresses/EditForm?id=" + command.getId();
        }
    }

    @PostMapping("SetAsMain")
    public String SetAsMain(@RequestParam("id") Long id, Model model, RedirectAttributes ra ){
        try {
            var command = new SetAddressAsMainCommand(id);
            addressesService.setAddressAsMain(command);
            ra.addFlashAttribute("success", "Pomyślnie udało się ustawić adres jako główny");
            return "redirect:/Addresses/EditForm?id=" + id;
        } catch (Exception ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/Addresses/EditForm?id=" + id;
        }
    }


    @GetMapping("/DeleteAddress")
    public String Delete(Model model, RedirectAttributes ra, Long id){
        Long ref = 0l;
        try{
            var command = new DeleteAddressCommand(id);
            var address = addressesService.getAddresses(new GetAddressesCommand().whereIdEqual(id)).getFirst();
            ref = address.getCustomer().getId();
            addressesService.deleteAddress(command);
            var date = LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            ra.addFlashAttribute("success", date + " • Pomyślnie udało się usunąć adres");
            return "redirect:/Customers/EditForm?id=" + ref;
        } catch(Exception ex){
            ra.addFlashAttribute(
                    "error",
                    "Nie udało się usunąć adresu. " + (ex.getMessage() != null ? ex.getMessage() : "")
            );
            return "redirect:/Customers/EditForm?id=" + ref;
        }
    }

}
