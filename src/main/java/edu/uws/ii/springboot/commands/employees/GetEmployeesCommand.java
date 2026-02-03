package edu.uws.ii.springboot.commands.employees;

import edu.uws.ii.springboot.models.Role;
import edu.uws.ii.springboot.models.User;
import edu.uws.ii.springboot.models.Warehouse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetEmployeesCommand {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String userName;
    private Long warehouseId;
    private Role.Types role;

    public GetEmployeesCommand(){

    }

    public GetEmployeesCommand whereRoleEquals(Role.Types role){
        this.role = role;
        return this;
    }

    public GetEmployeesCommand whereFirstNameEquals(String firstName){
        this.firstName = firstName;
        return this;
    }


    public GetEmployeesCommand whereLastNameEquals(String lastName){
        this.lastName = lastName;
        return this;
    }

    public GetEmployeesCommand whereEmailEquals(String email){
        this.email = email;
        return this;
    }

    public GetEmployeesCommand wherePhoneEquals(String phone) {
        this.phone = phone;
        return this;
    }

    public GetEmployeesCommand whereWarehouseEquals(Warehouse warehouse){
        this.warehouseId = warehouse.getId();
        return this;
    }

    public GetEmployeesCommand whereUserNameEquals(User user){
        this.userName =  user.getUsername();
        return this;
    }

}
