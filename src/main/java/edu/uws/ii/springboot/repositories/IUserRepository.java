package edu.uws.ii.springboot.repositories;

import edu.uws.ii.springboot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface IUserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
}
