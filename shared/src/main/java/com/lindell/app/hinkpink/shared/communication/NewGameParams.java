package com.lindell.app.hinkpink.shared.communication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lindell on 7/13/15.
 */
public class NewGameParams extends CommunicatorParams {
    private String hint;
    private int numSyllables;
    private boolean guessedCorrect;
    private List<String> guesses;
    private Long playerConnectionID;
    private Long connectionID;

    public NewGameParams() {
        hint = null;
        numSyllables = 0;
        guessedCorrect = false;
        guesses = new ArrayList<String>();
        playerConnectionID = 0L;
        connectionID = 0L;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getNumSyllables() {
        return numSyllables;
    }

    public void setNumSyllables(int numSyllables) {
        this.numSyllables = numSyllables;
    }

    public boolean isGuessedCorrect() {
        return guessedCorrect;
    }

    public void setGuessedCorrect(boolean guessedCorrect) {
        this.guessedCorrect = guessedCorrect;
    }

    public List<String> getGuesses() {
        return guesses;
    }

    public void setGuesses(List<String> guesses) {
        this.guesses = guesses;
    }

    public Long getPlayerConnectionID() {
        return playerConnectionID;
    }

    public void setPlayerConnectionID(Long playerConnectionID) {
        this.playerConnectionID = playerConnectionID;
    }

    public Long getConnectionID() {
        return connectionID;
    }

    public void setConnectionID(Long connectionID) {
        this.connectionID = connectionID;
    }
}
