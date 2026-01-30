package edu.uws.ii.springboot.commands.customers;

import edu.uws.ii.springboot.enums.CustomerTypeEnum;
import edu.uws.ii.springboot.models.Customer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class EditCustomerCommand {

    private  Long id;
    private String acronym;
    private String fullName;
    private String nip;
    private CustomerTypeEnum customerType;
    private String email;
    private String url;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate foundedOn;

    public EditCustomerCommand() {

    }

    public EditCustomerCommand(Customer customer) {
        this.acronym = customer.getAcronym();
        this.fullName = customer.getFullName();
        this.nip = customer.getNip();
        this.id = customer.getId();
        this.customerType = customer.getCustomerType();
        this.email = customer.getEmail();
        this.url = customer.getUrl();
        this.foundedOn = customer.getFoundedOn();
    }
}
