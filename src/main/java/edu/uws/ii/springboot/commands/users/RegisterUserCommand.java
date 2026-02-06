package edu.uws.ii.springboot.commands.users;

import edu.uws.ii.springboot.models.Employee;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserCommand {
    private String userName;
    private String password;
    private Long employeeId;

    RegisterUserCommand() {

    }

    public RegisterUserCommand configureUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public RegisterUserCommand configurePassword(String userName){
        this.userName = userName;
        return this;
    }

    public RegisterUserCommand configureEmployee(Employee employee) {
        this.employeeId = employee.getId();
        return this;
    }

}
