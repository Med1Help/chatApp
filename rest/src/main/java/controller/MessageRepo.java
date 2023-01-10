package controller;
import controller.message;
import controller.UserController;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepo extends CrudRepository<message,Long> {
  public List<message> findByuserId(int userId);
}
