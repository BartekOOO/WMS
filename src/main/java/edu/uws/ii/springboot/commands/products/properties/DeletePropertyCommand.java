package edu.uws.ii.springboot.commands.products.properties;

import edu.uws.ii.springboot.models.Property;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeletePropertyCommand {
    private Long id;

    public DeletePropertyCommand() {

    }

    public DeletePropertyCommand(Property property) {
        this.id = property.getId();
    }
}
