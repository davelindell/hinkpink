package com.lindell.app.hinkpink.shared.models;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by lindell on 5/31/15.
 */

@Entity
public class HinkPinkUser  {
    @Id Long id;
    @Index  String email;
     String password;

    public HinkPinkUser(){
        this.email = null;
        this.password = null;
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
