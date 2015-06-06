package com.lindell.app.hinkpink.backend.shared.communication;

/**
 * Created by lindell on 6/1/15.
 * Super class for the _Params methods that share
 * the email and password data members.
 *
 */
public class CommunicatorParams {
    private String email;
    private String password;

    public CommunicatorParams (String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
