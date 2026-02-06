package edu.uws.ii.springboot.commands.users;

import edu.uws.ii.springboot.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteUserCommand {

    private Long id;

    public  DeleteUserCommand(Long id)
    {
        this.id = id;
    }

    public DeleteUserCommand(User user){
        this.id=user.getId();
    }
}
