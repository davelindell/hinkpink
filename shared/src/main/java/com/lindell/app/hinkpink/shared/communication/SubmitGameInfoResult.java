package com.lindell.app.hinkpink.shared.communication;

/**
 * Created by lindell on 8/12/15.
 */
public class SubmitGameInfoResult {
    String information;
    boolean received;

    public SubmitGameInfoResult() {

    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }
}
