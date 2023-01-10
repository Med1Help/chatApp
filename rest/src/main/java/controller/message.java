package controller;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String message;
    private int userId;
    private String sessionId;

    public message(int id, String message, int userId, String sessionId) {
        this.id = id;
        this.message = message;
        this.userId = userId;
        this.sessionId = sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public message(String message, int userId) {
        this.message = message;
        this.userId = userId;
    }

    public message(int id, String message, int userId) {
        this.id = id;
        this.message = message;
        this.userId = userId;
    }

    public message() {
    }

    @Override
    public String toString() {
        return "message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", userId=" + userId +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public int getUserId() {
        return userId;
    }
}
