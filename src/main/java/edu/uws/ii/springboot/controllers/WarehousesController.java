package edu.uws.ii.springboot.controllers;

import edu.uws.ii.springboot.commands.products.GetProductsCommand;
import edu.uws.ii.springboot.commands.warehouses.GetWarehousesCommand;
import edu.uws.ii.springboot.interfaces.IWarehousesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Warehouses")
public class WarehousesController {

    private final IWarehousesService warehousesService;

    public WarehousesController(IWarehousesService warehousesService) {
        this.warehousesService = warehousesService;
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

        if (includeArchival == null) {
            cmd.whereIsNotArchival();
        }

        model.addAttribute("warehouses", warehousesService.getWarehouses(cmd));

        return "warehouses/getWarehouses";
    }


}
