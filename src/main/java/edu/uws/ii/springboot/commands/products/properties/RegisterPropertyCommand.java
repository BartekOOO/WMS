package edu.uws.ii.springboot.commands.products.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterPropertyCommand {
    private String propertyName;

    public RegisterPropertyCommand(String propertyName){
        this.propertyName = propertyName;
    }

    public RegisterPropertyCommand(){
    }
}
