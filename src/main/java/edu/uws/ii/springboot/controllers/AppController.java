package edu.uws.ii.springboot.controllers;


import edu.uws.ii.springboot.interfaces.IHistoryLogsService;
import edu.uws.ii.springboot.models.Customer;
import edu.uws.ii.springboot.repositories.ICustomersRepository;
import edu.uws.ii.springboot.repositories.IDocumentsRepository;
import edu.uws.ii.springboot.repositories.IProductsRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/App")
public class AppController {

    private final ICustomersRepository customersRepository;
    private final IProductsRepository productsRepository;
    private final IDocumentsRepository documentsRepository;
    private final IHistoryLogsService  historyLogsService;

    public AppController(ICustomersRepository customersRepository,  IProductsRepository productsRepository, IDocumentsRepository documentsRepository, IHistoryLogsService historyLogsService) {
        this.customersRepository = customersRepository;
        this.productsRepository = productsRepository;
        this.historyLogsService = historyLogsService;
        this.documentsRepository = documentsRepository;
    }


    @GetMapping
    public String index(Model model) {
        model.addAttribute("pageTitle", "WMS • Panel");
        model.addAttribute("content", "dashboard :: fragment");
        return "app";
    }


}
