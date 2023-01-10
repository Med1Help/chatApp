package controller;

import com.sun.security.auth.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;


import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class UserHandShakeHandler extends DefaultHandshakeHandler {
    private final Logger Log =  LoggerFactory.getLogger(UserHandShakeHandler.class);
    @Autowired
    UserRepo repo ;

    public UserHandShakeHandler(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        final String randomId = UUID.randomUUID().toString();
        Log.info("user with {} open a page "+randomId);
        int uId = Integer.parseInt(request.getHeaders().getFirst("UserId"));
        Log.info("Attributes :  "+uId);
        User user = repo.findById(uId);
        user.setSessionId(randomId);
        repo.save(user);
        Log.info("Attributes :  "+user.toString());
        return new UserPrincipal(randomId);
    }
}
