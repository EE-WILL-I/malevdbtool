package com.malevdb.Application.SessionManagement;

public class UserSession {
    public final String sessionId, userId;
    public UserSession(String sessId, String usrId) {
        sessionId = sessId;
        userId = usrId;
    }
}
