package edu.uws.ii.springboot.commands.warehouses;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetWarehousesCommand {

    private Long id;
    private String code;
    private String name;
    private Boolean isArchival;

    public GetWarehousesCommand() {

    }

    public GetWarehousesCommand whereCodeEqualst(String code) {
        this.code = code;
        return this;
    }

    public GetWarehousesCommand whereNameEqualst(String name) {
        this.name = name;
        return this;
    }

    public GetWarehousesCommand whereIsArchival() {
        this.isArchival = true;
        return this;
    }

    public GetWarehousesCommand whereIsNotArchival() {
        this.isArchival = false;
        return this;
    }

    public GetWarehousesCommand whereIdEquals(Long id) {
        this.id = id;
        return this;
    }


}
