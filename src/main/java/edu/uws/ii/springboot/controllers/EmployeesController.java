package edu.uws.ii.springboot.controllers;

import edu.uws.ii.springboot.commands.customers.RegisterCustomerCommand;
import edu.uws.ii.springboot.commands.employees.GetEmployeesCommand;
import edu.uws.ii.springboot.commands.employees.RegisterEmployeeCommand;
import edu.uws.ii.springboot.commands.warehouses.GetWarehousesCommand;
import edu.uws.ii.springboot.interfaces.IEmployeesService;
import edu.uws.ii.springboot.interfaces.IWarehousesService;
import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Customer;
import edu.uws.ii.springboot.models.Role;
import edu.uws.ii.springboot.repositories.IEmployeesRepository;
import edu.uws.ii.springboot.repositories.IWarehouseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/Employees")
public class EmployeesController {

    private final IEmployeesService  employeesService;
    private final IWarehousesService  warehousesService;

    public EmployeesController(IEmployeesService employeesService, IWarehousesService warehousesService) {
        this.employeesService = employeesService;
        this.warehousesService = warehousesService;
    }

    @GetMapping("/GetEmployees")
    public String GetEmployees(
            Model model,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Role.Types role
    ) {
        GetEmployeesCommand cmd = new GetEmployeesCommand();

        if (id != null && id != 0) cmd.setId(id);
        if (firstName != null && !firstName.trim().isEmpty()) cmd.setFirstName(firstName.trim());
        if (lastName != null && !lastName.trim().isEmpty()) cmd.setLastName(lastName.trim());
        if (email != null && !email.trim().isEmpty()) cmd.setEmail(email.trim());
        if (phone != null && !phone.trim().isEmpty()) cmd.setPhone(phone.trim());
        if (userName != null && !userName.trim().isEmpty()) cmd.setUserName(userName.trim());
        if (warehouseId != null && warehouseId != 0) cmd.setWarehouseId(warehouseId);
        if (role != null) cmd.setRole(role);

        model.addAttribute("employees", employeesService.getEmployees(cmd));

        var whCmd = new GetWarehousesCommand().whereIsNotArchival();
        model.addAttribute("warehouses", warehousesService.getWarehouses(whCmd));

        model.addAttribute("content", "employees/list :: fragment");
        model.addAttribute("pageTitle", "Pracownicy");

        return "app";
    }

    @GetMapping("/AddForm")
    public String AddForm(Model model){
        model.addAttribute("content", "Employees/AddForm :: fragment");
        model.addAttribute("command", new RegisterEmployeeCommand());
        model.addAttribute("title", "WMS • Dodawanie pracownika");
        return "app";
    }


    @PostMapping("/RegisterEmployee")
    public String Register(Model model, RedirectAttributes ra, @ModelAttribute RegisterEmployeeCommand command) {
        try {
            var result = employeesService.registerEmployee(command);
            ra.addFlashAttribute("success", "Pomyślnie udało się dodać pracownika '" + result.getFirstName()+ "'");
            return "redirect:/Employees/EditForm?id=" + result.getId();
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("content", "Employees/AddForm :: fragment");
            model.addAttribute("command", command);
            model.addAttribute("title", "WMS • Dodawanie pracownika");
            return "app";
        }
    }
}
