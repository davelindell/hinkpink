package com.lindell.app.hinkpink.shared.models;

/**
 * Created by lindell on 6/15/15.
 */

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class HinkPinkConnection implements Serializable {
    @Id
    private Long id;
    // describes id associated with email address (friend's)
    private Long playerID;
    // friend's associated connection ID
    private Long playerConnectionID;
    private String email;
    private String displayName;
    private List<Long> gameList;


    public HinkPinkConnection() {
        this.id = null;
        this.email = null;
        this.displayName = null;
        this.gameList = new ArrayList<Long>();
        this.playerID = null;
        this.playerConnectionID = null;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HinkPinkConnection that = (HinkPinkConnection) o;

        return email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<Long> getGameList() {
        return gameList;
    }

    public void setGameList(List<Long> gameList) {
        this.gameList = gameList;
    }

    public Long getPlayerID() {
        return playerID;
    }

    public void setPlayerID(Long playerID) {
        this.playerID = playerID;
    }

    public Long getPlayerConnectionID() {
        return playerConnectionID;
    }

    public void setPlayerConnectionID(Long playerConnectionID) {
        this.playerConnectionID = playerConnectionID;
    }
}
