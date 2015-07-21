package com.lindell.app.hinkpink.shared.communication;

/**
 * Created by lindell on 7/8/15.
 */
public class GetConnectionGamesParams extends CommunicatorParams {

    private Long connectionID;

    public GetConnectionGamesParams() {

    }

    public Long getConnectionID() {
        return connectionID;
    }

    public void setConnectionID(Long connectionID) {
        this.connectionID = connectionID;
    }
}
