package edu.uws.ii.springboot.config;

import edu.uws.ii.springboot.commands.AuthorizedCommandBase;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.WebDataBinder;

@ControllerAdvice
public class AuthorizedCommandBinderAdvice {

    @InitBinder
    public void injectUserCode(WebDataBinder binder) {
        Object target = binder.getTarget();
        if (!(target instanceof AuthorizedCommandBase cmd)) {
            return;
        }

        binder.setDisallowedFields("userCode");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userCode = (auth != null && auth.isAuthenticated()) ? auth.getName() : null;

        cmd.setUserCode(userCode);
    }
}
