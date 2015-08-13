package com.lindell.app.hinkpink.shared.communication;

/**
 * Created by lindell on 8/12/15.
 */
public class GetGameParams extends CommunicatorParams {

    Long gameID;

    public GetGameParams(){
        gameID = null;
    }

    public Long getGameID() {
        return gameID;
    }

    public void setGameID(Long gameID) {
        this.gameID = gameID;
    }
}
