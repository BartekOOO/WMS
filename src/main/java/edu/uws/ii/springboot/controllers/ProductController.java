package edu.uws.ii.springboot.controllers;

import edu.uws.ii.springboot.commands.addresses.EditAddressCommand;
import edu.uws.ii.springboot.commands.addresses.GetAddressesCommand;
import edu.uws.ii.springboot.commands.addresses.RegisterAddressCommand;
import edu.uws.ii.springboot.commands.customers.EditCustomerCommand;
import edu.uws.ii.springboot.commands.customers.GetCustomersCommand;
import edu.uws.ii.springboot.commands.customers.RegisterCustomerCommand;
import edu.uws.ii.springboot.commands.products.DeleteProductCommand;
import edu.uws.ii.springboot.commands.products.EditProductCommand;
import edu.uws.ii.springboot.commands.products.GetProductsCommand;
import edu.uws.ii.springboot.commands.products.RegisterProductCommand;
import edu.uws.ii.springboot.commands.products.properties.DeletePropertyCommand;
import edu.uws.ii.springboot.commands.products.properties.RegisterPropertyCommand;
import edu.uws.ii.springboot.commands.products.units.DeleteUnitCommand;
import edu.uws.ii.springboot.commands.products.units.EditUnitCommand;
import edu.uws.ii.springboot.commands.products.units.RegisterUnitCommand;
import edu.uws.ii.springboot.interfaces.IProductsService;
import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Product;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
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
    public String GetProducts(
            Model model,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String ean,
            @RequestParam(required = false) Boolean includeArchival,
            @RequestParam(required = false) Boolean isArchival
    ) {
        model.addAttribute("title", "WMS • Produkty");
        model.addAttribute("content", "products/list :: fragment");

        var command = new GetProductsCommand();

        if (isArchival != null) {
            if (isArchival) command.whereIsArchival();
            else command.whereIsNotArchival();
        } else {
            if (!Boolean.TRUE.equals(includeArchival)) {
                command.whereIsNotArchival();
            }
        }

        if (sku != null && !sku.isBlank()) command.setSku(sku.trim());
        if (name != null && !name.isBlank()) command.whereNameEquals(name.trim());
        if (brand != null && !brand.isBlank()) command.whereBrandEquals(brand.trim());
        if (ean != null && !ean.isBlank()) command.whereEanEquals(ean.trim());

        model.addAttribute("products", productsService.getProducts(command));
        return "app";
    }


    @GetMapping("/AddForm")
    public String AddForm(Model model) {
        model.addAttribute("content", "products/addForm :: fragment");
        var command = new RegisterProductCommand();

        model.addAttribute("command", command);
        model.addAttribute("title", "WMS • Dodawanie produktu");
        return "app";
    }

    @GetMapping("/EditForm")
    public String EditForm(Model model, @RequestParam(required = false) Long id) {
        var command = new GetProductsCommand().whereIdEquals(id);
        var productToEdit = productsService.getProducts(command).stream().findFirst().orElse(null);

        if (productToEdit == null) {
            model.addAttribute("error", "Nie znaleziono produktu.");
            model.addAttribute("content", "products/list :: fragment");
            model.addAttribute("title", "WMS • Produkty");
            model.addAttribute("products", productsService.getProducts(new GetProductsCommand().whereIsNotArchival()));
            return "app";
        }

        if (productToEdit.getUnits() != null) productToEdit.getUnits().size();
        if (productToEdit.getProperties() != null) productToEdit.getProperties().size();

        model.addAttribute("content", "products/editForm :: fragment");
        model.addAttribute("command", new EditProductCommand(productToEdit));
        model.addAttribute("product", productToEdit);
        model.addAttribute("title", "WMS • Edytowanie produktu");
        return "app";
    }



    @PostMapping("/RegisterProduct")
    public String Register(
            Model model,
            RedirectAttributes ra,
            @ModelAttribute RegisterProductCommand command,
            @RequestParam(value = "photo", required = false) MultipartFile photo
    ) {
        try {
            if (photo != null && !photo.isEmpty()) {
                var p = command.getProduct();
                p.setPhotoContent(photo.getBytes());
                p.setPhotoContentType(photo.getContentType());
                p.setPhotoName(photo.getOriginalFilename());
            }

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
    public String Edit(
            Model model,
            RedirectAttributes ra,
            @ModelAttribute EditProductCommand command,
            @RequestParam(value = "photo", required = false) MultipartFile photo
    ) {
        try {
            if (photo != null && !photo.isEmpty()) {
                command.setPhotoContent(photo.getBytes());
                command.setPhotoContentType(photo.getContentType());
                command.setPhotoName(photo.getOriginalFilename());
            }

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

    @GetMapping("/Photo")
    @ResponseBody
    public ResponseEntity<byte[]> Photo(@RequestParam Long id) {
        var p = productsService.getProducts(new GetProductsCommand().whereIdEquals(id))
                .stream().findFirst().orElse(null);

        if (p == null || p.getPhotoContent() == null || p.getPhotoContent().length == 0) {
            return ResponseEntity.notFound().build();
        }

        MediaType mt;
        try {
            mt = (p.getPhotoContentType() != null && !p.getPhotoContentType().isBlank())
                    ? MediaType.parseMediaType(p.getPhotoContentType())
                    : MediaType.APPLICATION_OCTET_STREAM;
        } catch (Exception e) {
            mt = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mt)
                .cacheControl(CacheControl.noCache())
                .body(p.getPhotoContent());
    }


    @PostMapping("/RegisterUnit")
    public String RegisterUnit(RedirectAttributes ra,
                               @RequestParam Long productId,
                               @RequestParam String unitName,
                               @RequestParam BigDecimal multiplier) {
        try {
            var u = new edu.uws.ii.springboot.models.Unit();
            u.setUnitName(unitName);
            u.setMultiplier(multiplier);

            var p = new edu.uws.ii.springboot.models.Product();
            p.setId(productId);
            u.setProduct(p);

            var cmd = new RegisterUnitCommand();
            cmd.setUnit(u);

            productsService.registerUnit(cmd);
            ra.addFlashAttribute("success", "Dodano jednostkę: " + unitName);
        } catch (Exception ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/Products/EditForm?id=" + productId + "#units";
    }

    @PostMapping("/EditUnit")
    public String EditUnit(RedirectAttributes ra,
                           @RequestParam Long productId,
                           @RequestParam Long id,
                           @RequestParam String unitName,
                           @RequestParam java.math.BigDecimal multiplier) {
        try {
            var cmd = new EditUnitCommand();
            cmd.setId(id);
            cmd.setUnitName(unitName);
            cmd.setUnitMultiplier(multiplier);

            productsService.editUnit(cmd);
            ra.addFlashAttribute("success", "Zmieniono jednostkę: " + unitName);
        } catch (Exception ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/Products/EditForm?id=" + productId + "#units";
    }

    @GetMapping("/DeleteUnit")
    public String DeleteUnit(RedirectAttributes ra,
                             @RequestParam Long productId,
                             @RequestParam Long id) {
        try {
            var cmd = new DeleteUnitCommand(id);
            productsService.deleteUnit(cmd);
            ra.addFlashAttribute("success", "Usunięto jednostkę.");
        } catch (Exception ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/Products/EditForm?id=" + productId + "#units";
    }


    @PostMapping("/RegisterProperty")
    public String RegisterProperty(RedirectAttributes ra,
                                   @RequestParam Long productId,
                                   @RequestParam String propertyName) {
        try {
            var cmd = new RegisterPropertyCommand();
            cmd.setProductId(productId);
            cmd.setPropertyName(propertyName);

            productsService.registerProperty(cmd);
            ra.addFlashAttribute("success", "Dodano cechę: " + propertyName);
        } catch (Exception ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/Products/EditForm?id=" + productId + "#props";
    }

    @PostMapping("/EditProperty")
    public String EditProperty(RedirectAttributes ra,
                               @RequestParam Long productId,
                               @RequestParam Long id,
                               @RequestParam String propertyName) {
        try {
            var cmd = new edu.uws.ii.springboot.commands.products.properties.EditPropertyCommand();
            cmd.setId(id);
            cmd.setPropertyName(propertyName);

            productsService.editProperty(cmd);
            ra.addFlashAttribute("success", "Zmieniono cechę: " + propertyName);
        } catch (Exception ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/Products/EditForm?id=" + productId + "#props";
    }

    @GetMapping("/DeleteProperty")
    public String DeleteProperty(RedirectAttributes ra,
                                 @RequestParam Long productId,
                                 @RequestParam Long id) {
        try {
            var cmd = new DeletePropertyCommand(id);
            productsService.deleteProperty(cmd);
            ra.addFlashAttribute("success", "Usunięto cechę.");
        } catch (Exception ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/Products/EditForm?id=" + productId + "#props";
    }

    @GetMapping("/DeleteProduct")
    public String DeleteProduct(RedirectAttributes ra, @RequestParam Long id) {
        try {
            var cmd = new DeleteProductCommand(id);
            productsService.deleteProduct(cmd);
            ra.addFlashAttribute("success", "Produkt został zarchiwizowany.");
            return "redirect:/Products/GetProducts";
        } catch (Exception ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/Products/EditForm?id=" + id;
        }
    }


}
