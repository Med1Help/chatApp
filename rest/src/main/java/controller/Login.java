package controller;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

public class Login {
    private String response;
    private String UserName;
    private String tok;
    private String sessionId;
    private int id;

    public Login(String response, String userName, String tok,String sessionId, int id) {
        this.response = response;
        UserName = userName;
        this.sessionId = sessionId;
        this.id = id;
        this.tok = tok ;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Login() {
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public int getId() {
        return id;
    }

    public Login(String response, String userName, int id) {
        this.response = response;
        UserName = userName;
        this.id = id;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Login(String response) {
        this.response = response;
    }

    public String getTok() {
        return tok;
    }

    public void setTok(String tok) {
        this.tok = tok;
    }
}
