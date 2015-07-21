package com.lindell.app.hinkpink.shared.models;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


import java.util.LinkedList;
import java.util.List;

/**
 * Created by lindell on 5/31/15.
 */

@Entity
public class HinkPinkUser   {
    @Id private Long id;
    @Index  private String email;
    private String password;
    private String displayName;

    private List<Long> connectionList;

    private List<Long> pendingConnectionList;

    private List<Long> incomingConnectionList;

    public HinkPinkUser(){
        this.email = null;
        this.password = null;
        this.displayName = null;
        this.connectionList = new LinkedList<Long>();
        this.pendingConnectionList = new LinkedList<Long>();
        this.incomingConnectionList = new LinkedList<Long>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HinkPinkUser that = (HinkPinkUser) o;

        return email.equals(that.email);

    }

    @Override
    public int hashCode() {
        return email.hashCode();
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

    public List<Long> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<Long> connection_list) {
        this.connectionList = connection_list;
    }

    public List<Long> getPendingConnectionList() {
        return pendingConnectionList;
    }

    public void setPendingConnectionList(List<Long> pendingConnectionList) {
        this.pendingConnectionList = pendingConnectionList;
    }

    public List<Long> getIncomingConnectionList() {
        return incomingConnectionList;
    }

    public void setIncomingConnectionList(List<Long> incomingConnectionList) {
        this.incomingConnectionList = incomingConnectionList;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Long getId() {
        return id;
    }
}
