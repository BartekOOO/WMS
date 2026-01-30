package edu.uws.ii.springboot.commands.products;


import edu.uws.ii.springboot.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditProductCommand {

    private Long id;
    private byte[] photoContent;
    private String photoName;
    private String photoContentType;
    private String sku;
    private String name;
    private String unit;
    private String ean;
    private String description;
    private String brand;


    public EditProductCommand(Product product) {
        this.id = product.getId();
        this.photoContent = product.getPhotoContent();
        this.photoName = product.getPhotoName();
        this.photoContentType = product.getPhotoContentType();
        this.sku = product.getSku();
        this.name = product.getName();
        this.unit = product.getUnit();
        this.ean = product.getEan();
        this.description = product.getDescription();
        this.brand = product.getBrand();
    }

    public EditProductCommand() {

    }
}
