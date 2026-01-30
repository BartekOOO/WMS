package edu.uws.ii.springboot.commands.customers;

import edu.uws.ii.springboot.enums.CustomerTypeEnum;
import lombok.Getter;

import java.util.Optional;

@Getter
public class GetCustomersCommand {
    private Long id;
    private String acronym;
    private String name;
    private Boolean isMain;
    private String nip;
    private Optional<CustomerTypeEnum> customerType;
    private Boolean isArchival;

    public GetCustomersCommand() {

    }

    public GetCustomersCommand whereIdEquals(Long id)
    {
        this.id=id;
        return this;
    }

    public GetCustomersCommand whereAcronymEquals(String code)
    {
        this.acronym=code;
        return this;
    }

    public GetCustomersCommand whereNameEquals(String name)
    {
        this.name=name;
        return this;
    }

    public GetCustomersCommand whereIsMain(Boolean isMain)
    {
        this.isMain=isMain;
        return this;
    }

    public GetCustomersCommand whereNipEqual(String nip){
        this.nip = nip;
        return this;
    }

    public GetCustomersCommand whereCustomerTypeEquals(Optional<CustomerTypeEnum> customerType)
    {
        this.customerType = customerType;
        return this;
    }

    public GetCustomersCommand whereIsArchival()
    {
        this.isArchival=true;
        return this;
    }

    public GetCustomersCommand whereIsNotArchival()
    {
        this.isArchival=false;
        return this;
    }

}
