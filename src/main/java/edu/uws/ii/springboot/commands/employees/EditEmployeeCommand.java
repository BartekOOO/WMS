package edu.uws.ii.springboot.commands.employees;

import edu.uws.ii.springboot.models.Employee;

public class EditEmployeeCommand {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public EditEmployeeCommand() {

    }

    public EditEmployeeCommand(Employee employee) {

    }
}
