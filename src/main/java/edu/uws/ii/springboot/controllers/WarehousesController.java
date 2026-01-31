package edu.uws.ii.springboot.controllers;

import edu.uws.ii.springboot.commands.customers.GetCustomersCommand;
import edu.uws.ii.springboot.commands.products.GetProductsCommand;
import edu.uws.ii.springboot.commands.warehouses.GetWarehousesCommand;
import edu.uws.ii.springboot.commands.warehouses.RegisterWarehouseCommand;
import edu.uws.ii.springboot.interfaces.ICustomersService;
import edu.uws.ii.springboot.interfaces.IWarehousesService;
import edu.uws.ii.springboot.models.Address;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Warehouses")
public class WarehousesController {

    private final IWarehousesService warehousesService;
    private final ICustomersService customersService;

    public WarehousesController(IWarehousesService warehousesService, ICustomersService customersService) {
        this.warehousesService = warehousesService;
        this.customersService = customersService;
    }

    @GetMapping("/GetWarehouses")
    public String GetWarehouses(
            Model model,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean includeArchival
    ) {
        GetWarehousesCommand cmd = new GetWarehousesCommand();

        if (code != null && !code.trim().isEmpty()) cmd.whereCodeEqualst(code.trim() + "%");
        if (name != null && !name.trim().isEmpty()) cmd.whereNameEqualst("%" + name.trim() + "%");

        if (includeArchival == null) cmd.whereIsNotArchival();

        model.addAttribute("warehouses", warehousesService.getWarehouses(cmd));
        model.addAttribute("content", "warehouses/list :: fragment");
        model.addAttribute("pageTitle", "Magazyny");

        return "app";
    }


    @GetMapping("/AddForm")
    public String AddForm(Model model) {

        model.addAttribute("title", "WMS • Dodawanie magazynu");
        model.addAttribute("content", "warehouses/addForm :: fragment");

        var cmd = new RegisterWarehouseCommand();
        cmd.configureAddress(new Address());
        model.addAttribute("command", cmd);

        var q = new GetCustomersCommand().whereIsMain(true);

        var companies = customersService.getCustomers(q);
        var company = companies.isEmpty() ? null : companies.getFirst();

        var addresses = (company == null || company.getAddresses() == null)
                ? java.util.List.<Address>of()
                : company.getAddresses().stream()
                .filter(a -> a != null && !a.isArchival())
                .toList();

        model.addAttribute("companyCustomer", company);
        model.addAttribute("companyAddresses", addresses);

        return "app";
    }

}
