package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class SocketController {
    @Autowired
    private final SimpMessagingTemplate tmplate;

    public SocketController(SimpMessagingTemplate tmplate) {
        this.tmplate = tmplate;
    }


    @SendToUser("/topic/message")
    public message broadcastMessage(@RequestBody message msg , final Principal principal){
        System.out.println("send : "+msg.toString());
        return new message(msg.getMessage()+" : "+principal.getName(),msg.getUserId());
    }
    @MessageMapping("/chat")
    public void unicastMessage(@RequestBody message msg){
        System.out.println(msg.toString()+" From chat to "+msg.getMessage());
        tmplate.convertAndSendToUser( msg.getSessionId(),"/topic/message",new message(msg.getMessage(),msg.getUserId()));
    }
}
