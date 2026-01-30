package edu.uws.ii.springboot.commands.products;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
public class GetProductsCommand {

    private Long id;
    private Boolean isArchival;
    private String sku;
    private String name;
    private String ean;
    private String brand;

    public GetProductsCommand() {

    }

    public GetProductsCommand whereBrandEquals(String brand) {
        this.brand = brand;
        return this;
    }

    public GetProductsCommand whereEanEquals(String ean) {
        this.ean = ean;
        return this;
    }

    public GetProductsCommand whereNameEquals(String name) {
        this.name = name;
        return this;
    }

    public GetProductsCommand whereSkuEquals(String sku) {
        this.sku = sku;
        return this;
    }

    public GetProductsCommand whereIdEquals(Long id) {
        this.id = id;
        return this;
    }

    public GetProductsCommand whereIsArchival() {
        this.isArchival = true;
        return this;
    }

    public GetProductsCommand whereIsNotArchival() {
        this.isArchival = false;
        return this;
    }
}
