package edu.uws.ii.springboot.commands.products.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPropertiesCommand {
    private Long id;
    private String propertyName;
    private Long productId;

    public GetPropertiesCommand whereIdEquals(Long id)
    {
        this.id=id;
        return this;
    }

    public GetPropertiesCommand wherePropertyEquals(String propertyName)
    {
        this.propertyName=propertyName;
        return this;
    }

    public GetPropertiesCommand whereProductId(Long productId)
    {
        this.productId=productId;
        return this;
    }
}
