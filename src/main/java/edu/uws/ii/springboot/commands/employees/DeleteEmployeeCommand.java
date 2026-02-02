package edu.uws.ii.springboot.commands.employees;

import edu.uws.ii.springboot.models.Employee;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteEmployeeCommand {

    private Long id;

    public DeleteEmployeeCommand(Long id) {
        this.id = id;
    }

    public DeleteEmployeeCommand(Employee employee) {
        this.id = employee.getId();
    }
}
