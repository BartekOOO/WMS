package edu.uws.ii.springboot.controllers;

import edu.uws.ii.springboot.interfaces.IWarehousesService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Warehouses")
public class WarehousesController {

    private final IWarehousesService warehousesService;

    public WarehousesController(IWarehousesService warehousesService) {
        this.warehousesService = warehousesService;
    }




}
