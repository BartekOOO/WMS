package edu.uws.ii.springboot.commands.employees;

import edu.uws.ii.springboot.models.Employee;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterEmployeeCommand {

    private Employee employee;

    public RegisterEmployeeCommand(Employee employee) {
        this.employee = employee;
    }

    public RegisterEmployeeCommand() {

    }


}
