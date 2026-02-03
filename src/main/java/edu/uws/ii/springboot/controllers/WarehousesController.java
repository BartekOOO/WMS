package edu.uws.ii.springboot.controllers;

import edu.uws.ii.springboot.commands.customers.DeleteCustomerCommand;
import edu.uws.ii.springboot.commands.customers.GetCustomersCommand;
import edu.uws.ii.springboot.commands.products.GetProductsCommand;
import edu.uws.ii.springboot.commands.products.units.DeleteUnitCommand;
import edu.uws.ii.springboot.commands.sectors.DeleteSectorCommand;
import edu.uws.ii.springboot.commands.sectors.EditSectorCommand;
import edu.uws.ii.springboot.commands.sectors.RegisterSectorCommand;
import edu.uws.ii.springboot.commands.warehouses.DeleteWarehouseCommand;
import edu.uws.ii.springboot.commands.warehouses.EditWarehouseCommand;
import edu.uws.ii.springboot.commands.warehouses.GetWarehousesCommand;
import edu.uws.ii.springboot.commands.warehouses.RegisterWarehouseCommand;
import edu.uws.ii.springboot.enums.SectorTypeEnum;
import edu.uws.ii.springboot.interfaces.ICustomersService;
import edu.uws.ii.springboot.interfaces.IWarehousesService;
import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Sector;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

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

    @PostMapping("/RegisterWarehouse")
    public String RegisterWarehouse(
            Model model,
            RedirectAttributes ra,
            @ModelAttribute RegisterWarehouseCommand command
    ) {
        try {
            var result = warehousesService.registerWarehouse(command);

            ra.addFlashAttribute("success",
                    "Pomyślnie udało się dodać magazyn '" + result.getCode() + "'");

            return "redirect:/Warehouses/EditForm?id=" + result.getId();

        } catch (Exception ex) {

            model.addAttribute("error", ex.getMessage());
            model.addAttribute("title", "WMS • Dodawanie magazynu");
            model.addAttribute("content", "warehouses/addForm :: fragment");

            if (command.getAddress() == null) {
                command.configureAddress(new edu.uws.ii.springboot.models.Address());
            }
            model.addAttribute("command", command);

            var q = new edu.uws.ii.springboot.commands.customers.GetCustomersCommand()
                    .whereIsMain(true)
                    .whereIsNotArchival();

            var companies = customersService.getCustomers(q);
            var company = companies.isEmpty() ? null : companies.getFirst();

            var addresses = (company == null || company.getAddresses() == null)
                    ? java.util.List.<edu.uws.ii.springboot.models.Address>of()
                    : company.getAddresses().stream()
                    .filter(a -> a != null && !a.isArchival())
                    .toList();

            model.addAttribute("companyCustomer", company);
            model.addAttribute("companyAddresses", addresses);

            return "app";
        }
    }

    @PostMapping("/EditWarehouse")
    public String Edit(
            Model model,
            RedirectAttributes ra,
            @ModelAttribute EditWarehouseCommand command
    ) {
        try {
            warehousesService.editWarehouse(command);

            ra.addFlashAttribute("success",
                    "Pomyślnie udało się edytować magazyn");

            return "redirect:/Warehouses/EditForm?id=" + command.getId();

        } catch (Exception ex) {
            var warehouse = warehousesService.getWarehouses(new GetWarehousesCommand().whereIdEquals(command.getId())).getFirst();
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("title", "WMS • Edycja magazynu");
            model.addAttribute("content", "warehouses/editForm :: fragment");
            model.addAttribute("address", warehouse.getAddress());
            model.addAttribute("employees", warehouse.getEmployees());
            model.addAttribute("sectors",  warehouse.getSectors());
            model.addAttribute("command", command);
            return "app";
        }
    }

    @GetMapping("/EditForm")
    public String EditForm(@RequestParam("id") Long id, Model model, RedirectAttributes ra) {

        model.addAttribute("title", "WMS • Edycja magazynu");
        model.addAttribute("content", "warehouses/editForm :: fragment");

        var warehouse = warehousesService.getWarehouses(new GetWarehousesCommand().whereIdEquals(id)).getFirst();
        var cmd = new EditWarehouseCommand().configureWarehouse(warehouse);

        model.addAttribute("address", warehouse.getAddress());
        model.addAttribute("employees", warehouse.getEmployees());
        model.addAttribute("sectors",  warehouse.getSectors());

        model.addAttribute("command", cmd);

        return "app";
    }

    @GetMapping("/DeleteWarehouse")
    public String Delete(RedirectAttributes ra,
                             @RequestParam Long id) {
        try{
            var command = new DeleteWarehouseCommand(id);
            var warehouse = warehousesService.getWarehouses(new GetWarehousesCommand().whereIdEquals(id)).getFirst();
            warehousesService.deleteWarehouse(command);
            var date = LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            ra.addFlashAttribute("success", date + " • Pomyślnie udało się usunąć magazyn '" + warehouse.getCode() + "'");
            return "redirect:/Warehouses/GetWarehouses?isArchival=false";
        } catch(Exception ex){
            ra.addFlashAttribute(
                    "error",
                    "Nie udało się usunąć magazynu. " + (ex.getMessage() != null ? ex.getMessage() : "")
            );
            return "redirect:/Warehouses/EditForm?id=" + id;
        }


    }


    @PostMapping("/UpsertSector")
    public String UpsertSector(
            RedirectAttributes ra,
            @RequestParam Long warehouseId,
            @RequestParam(required = false) Long id,
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam(required = false) String type
    ) {
        try {
            if (id == null) {

                Sector s = new Sector();
                s.setCode(code.trim());
                s.setName(name.trim());
                s.setType(SectorTypeEnum.Normal);

                RegisterSectorCommand cmd = new RegisterSectorCommand()
                        .configureWarehouse(warehouseId)
                        .configureSector(s);

                var created = warehousesService.registerSector(cmd);
                ra.addFlashAttribute("success", "Dodano sektor '" + created.getCode() + "'.");
            } else {
                EditSectorCommand cmd = new EditSectorCommand();
                cmd.setId(id);
                cmd.setCode(code.trim());
                cmd.setName(name.trim());

                warehousesService.editSector(cmd);
                ra.addFlashAttribute("success", "Zaktualizowano sektor '" + code.trim() + "'.");
            }

            return "redirect:/Warehouses/EditForm?id=" + warehouseId;

        } catch (Exception ex) {
            ra.addFlashAttribute("error",
                    "Nie udało się zapisać sektora. " + (ex.getMessage() != null ? ex.getMessage() : ""));
            return "redirect:/Warehouses/EditForm?id=" + warehouseId;
        }
    }


    @GetMapping("/DeleteSector")
    public String DeleteSector(
            RedirectAttributes ra,
            @RequestParam Long warehouseId,
            @RequestParam Long id
    ) {
        try {
            var cmd = new DeleteSectorCommand().configureSector(id);
            warehousesService.deleteSector(cmd);

            ra.addFlashAttribute("success", "Usunięto sektor.");
            return "redirect:/Warehouses/EditForm?id=" + warehouseId;

        } catch (Exception ex) {
            ra.addFlashAttribute("error",
                    "Nie udało się usunąć sektora. " + (ex.getMessage() != null ? ex.getMessage() : ""));
            return "redirect:/Warehouses/EditForm?id=" + warehouseId;
        }
    }
}
