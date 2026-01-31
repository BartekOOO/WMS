package edu.uws.ii.springboot.commands.products.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPropertiesCommand {
    private Long id;
    private String propertyName;
    private Long productId;
}
