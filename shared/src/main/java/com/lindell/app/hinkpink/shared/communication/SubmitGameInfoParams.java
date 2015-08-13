package com.lindell.app.hinkpink.shared.communication;

/**
 * Created by lindell on 8/12/15.
 */
public class SubmitGameInfoParams extends CommunicatorParams {

    String information;
    boolean isGuess; // else is hint
    Long gameID;

    public SubmitGameInfoParams() {
        information = null;
        isGuess = false;
        gameID = null;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public boolean isGuess() {
        return isGuess;
    }

    public void setIsGuess(boolean isGuess) {
        this.isGuess = isGuess;
    }

    public Long getGameID() {
        return gameID;
    }

    public void setGameID(Long gameID) {
        this.gameID = gameID;
    }
}
