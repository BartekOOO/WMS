package edu.uws.ii.springboot.commands.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordCommand {

    public String userName;
    public String newPassword;
    public String oldPassword;

    public ChangePasswordCommand(String userName, String newPassword, String oldPassword) {
        this.userName = userName;
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
    }

    public ChangePasswordCommand() {

    }

}
