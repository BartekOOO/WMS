package edu.uws.ii.springboot.controllers;

import edu.uws.ii.springboot.commands.addresses.EditAddressCommand;
import edu.uws.ii.springboot.commands.addresses.GetAddressesCommand;
import edu.uws.ii.springboot.commands.addresses.RegisterAddressCommand;
import edu.uws.ii.springboot.commands.customers.EditCustomerCommand;
import edu.uws.ii.springboot.commands.customers.GetCustomersCommand;
import edu.uws.ii.springboot.commands.customers.RegisterCustomerCommand;
import edu.uws.ii.springboot.commands.products.EditProductCommand;
import edu.uws.ii.springboot.commands.products.GetProductsCommand;
import edu.uws.ii.springboot.commands.products.RegisterProductCommand;
import edu.uws.ii.springboot.interfaces.IProductsService;
import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/Products")
public class ProductController {

    private final IProductsService productsService;

    public ProductController(IProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping("/GetProducts")
    public String GetProducts(Model model, Optional<Boolean> isArchival){
        model.addAttribute("title", "WMS • Produkty");
        model.addAttribute("content", "products/list :: fragment");
        var command = new GetProductsCommand();
        if(isArchival.isPresent()){
            if(isArchival.get()){
                command.whereIsArchival();
            } else{
                command.whereIsNotArchival();
            }
        }
        model.addAttribute("customers", productsService.getProducts(command));
        return "app";
    }


    @GetMapping("/AddForm")
    public String AddForm(Model model){
        model.addAttribute("content", "products/addForm :: fragment");
        var command = new RegisterProductCommand();

        model.addAttribute("command", command);
        model.addAttribute("title", "WMS • Dodawanie produktu");
        return "app";
    }

    @GetMapping("/EditForm")
    public String EditForm(Model model, @RequestParam(required = false) Long id){
        var command = new GetProductsCommand();
        command.whereIdEquals(id);
        var productToEdit = productsService.getProducts(command).getFirst();

        model.addAttribute("content", "products/editForm :: fragment");
        model.addAttribute("command", new EditProductCommand(productToEdit));
        model.addAttribute("title", "WMS • Edytowanie produktu");
        return "app";
    }


    @PostMapping("/RegisterProduct")
    public String Register(Model model, RedirectAttributes ra, @ModelAttribute RegisterProductCommand command) {
        try {
            var result = productsService.registerProduct(command);
            ra.addFlashAttribute("success", "Pomyślnie udało się dodać '" + result.getName() + "'");
            return "redirect:/Products/EditForm?id=" + result.getId();
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("content", "Products/AddForm :: fragment");
            model.addAttribute("command", command);
            model.addAttribute("title", "WMS • Dodawanie produktu");
            return "app";
        }
    }


    @PostMapping("/EditProduct")
    public String Edit(Model model, RedirectAttributes ra, @ModelAttribute EditProductCommand command) {
        try {
            productsService.editProduct(command);
            var date = LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            ra.addFlashAttribute("success", date + " • Pomyślnie zapisano zmiany");
            return "redirect:/Products/EditForm?id=" + command.getId();
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("content", "Products/EditForm :: fragment");
            model.addAttribute("command", command);
            model.addAttribute("title", "WMS • Edytowanie produktu");
            return "app";
        }
    }

}
