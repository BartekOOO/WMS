package edu.uws.ii.springboot.commands.products.properties;

import edu.uws.ii.springboot.models.Property;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditPropertyCommand {

    private Long id;
    private String propertyName;

    public EditPropertyCommand(){

    }

    public  EditPropertyCommand(Property property) {
        this.id = property.getId();
        this.propertyName = property.getPropertyName();
    }
}
