package controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@EnableAutoConfiguration
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserRepo repo ;
    @Autowired
    MessageRepo mrepo ;
    @GetMapping("/io")
    public void test(){
        System.out.println("yes");
    }
    @PostMapping("/addU")
    public ResponseEntity<User> addUser(@RequestBody User myuser){
        System.out.println(myuser.toString());
        try{
            User returnedUser = (User) repo.save(new User(myuser.getUserName(), myuser.getPass()));
            return new ResponseEntity<>(returnedUser, HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/getU")
    public Login getUser(@RequestBody User myuser){
        System.out.println("incoming request "+myuser.getUserName()+"  "+myuser.getPass());
        try{
            List<User> users = (List<User>) repo.findByUserNameAndPass(myuser.getUserName(), myuser.getPass());
            if(users.isEmpty()){
                System.out.println("No user found");
                return new Login("false","0",0);
            }
                User user = users.get(0);
                System.out.println("No user found");
                return new Login("true",user.getUserName(),user.getSessionId(),user.getId());
        }catch(Exception e){
            return new Login("false","0",0);
        }
    }
    @GetMapping("/getMessage/{id}")
    public ResponseEntity<List<message>> get(@PathVariable(value = "id") int id) {
        List<message> msgs = mrepo.findByuserId(id);
      try{
        if (msgs.isEmpty()) {
            System.out.println("No user found");
            return new ResponseEntity<>(msgs, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(msgs, HttpStatus.OK);
    }catch(Exception e){
            return new ResponseEntity<>(msgs,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/send")
    public Login send(@RequestBody message msg){
        try{
            System.out.println("msg : "+msg.toString());
            message msgr = (message) mrepo.save(new message(msg.getMessage(), msg.getUserId()));
            return new Login("true", "0", 0);
        }catch(Exception e){
            System.out.println(e.toString());
            return new Login("false","0",0);
        }
    }
}
