package edu.uws.ii.springboot.controllers;

import edu.uws.ii.springboot.commands.customers.DeleteCustomerCommand;
import edu.uws.ii.springboot.commands.customers.EditCustomerCommand;
import edu.uws.ii.springboot.commands.customers.GetCustomersCommand;
import edu.uws.ii.springboot.commands.customers.RegisterCustomerCommand;
import edu.uws.ii.springboot.interfaces.ICustomersService;
import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/Customers")
public class CustomersController {

    private ICustomersService customersService;

    public CustomersController(ICustomersService customersService) {
        this.customersService = customersService;
    }


    @GetMapping("/GetCustomers")
    public String GetCustomers(Model model, Optional<Boolean> isArchival){
        model.addAttribute("title", "WMS • Kontrahenci");
        model.addAttribute("content", "customers/list :: fragment");
        var command = new GetCustomersCommand();
        if(isArchival.isPresent()){
            if(isArchival.get()){
                command.whereIsArchival();
            } else{
                command.whereIsNotArchival();
            }
        }
        model.addAttribute("customers", customersService.getCustomers(command));
        return "app";
    }

    @GetMapping("/AddForm")
    public String AddForm(Model model){
        model.addAttribute("content", "Customers/AddForm :: fragment");
        model.addAttribute("command", new RegisterCustomerCommand(new Customer(), new Address()));
        model.addAttribute("title", "WMS • Dodawanie kontrahenta");
        return "app";
    }

    @GetMapping("/EditForm")
    public String EditForm(Model model, @RequestParam(required = false) Long id){
        var command = new GetCustomersCommand();
        command.whereIdEquals(id);
        var customerToEdit = customersService.getCustomers(command).getFirst();

        var activeAddresses = customerToEdit.getAddresses()
                .stream()
                .filter(a -> a != null && !a.isArchival())
                .toList();
        customerToEdit.setAddresses(activeAddresses);
        model.addAttribute("content", "Customers/EditForm :: fragment");
        model.addAttribute("command", new EditCustomerCommand(customerToEdit));
        model.addAttribute("customer", customerToEdit);
        model.addAttribute("title", "WMS • Dodawanie kontrahenta");
        return "app";
    }

    @PostMapping("/RegisterCustomer")
    public String Register(Model model, RedirectAttributes ra, @ModelAttribute RegisterCustomerCommand command) {
        try {
            var result = customersService.registerCustomer(command);
            ra.addFlashAttribute("success", "Pomyślnie udało się dodać '" + result.getAcronym() + "'");
            return "redirect:/Customers/EditForm?id=" + result.getId();
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("content", "Customers/AddForm :: fragment");
            model.addAttribute("command", command);
            model.addAttribute("title", "WMS • Dodawanie kontrahenta");
            return "app";
        }
    }


    @PostMapping("/EditCustomer")
    public String Edit(Model model, RedirectAttributes ra, @ModelAttribute EditCustomerCommand command) {
        try {
            customersService.editCustomer(command);
            var date = LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            ra.addFlashAttribute("success", date + " • Pomyślnie zapisano zmiany");
            return "redirect:/Customers/EditForm?id=" + command.getId();
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("content", "Customers/EditForm :: fragment");
            model.addAttribute("command", command);
            model.addAttribute("title", "WMS • Edytowanie kontrahenta");

            var q = new GetCustomersCommand();
            q.whereIdEquals(command.getId());
            var list = customersService.getCustomers(q);
            model.addAttribute("customer", list.isEmpty() ? null : list.getFirst());

            return "app";
        }
    }


    @GetMapping("/DeleteCustomer")
    public String Delete(Model model, RedirectAttributes ra, Long id){
        try{
            var command = new DeleteCustomerCommand(id);
            var customer = customersService.getCustomers(new GetCustomersCommand().whereIdEquals(id)).getFirst();
            customersService.deleteCustomer(command);
            var date = LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            ra.addFlashAttribute("success", date + " • Pomyślnie udało się usunąć kontrahenta '" + customer.getAcronym() + "'");
            return "redirect:/Customers/GetCustomers?isArchival=false";
        } catch(Exception ex){
            ra.addFlashAttribute(
                    "error",
                    "Nie udało się usunąć kontrahenta. " + (ex.getMessage() != null ? ex.getMessage() : "")
            );
            return "redirect:/Customers/EditForm?id=" + id;
        }
    }


}
