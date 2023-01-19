package controller;

import controller.User;
import controller.UserController;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepo extends CrudRepository<User,Long> {
    List<User> findByUserName(String name);
    List<User> findByUserNameAndPass(String name,String pass);
    List<User> findAll();

    User findById(int userId);
}
