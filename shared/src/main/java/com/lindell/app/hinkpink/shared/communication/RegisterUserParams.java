package com.lindell.app.hinkpink.shared.communication;

/**
 * Created by lindell on 7/8/15.
 */
public class RegisterUserParams extends CommunicatorParams {

    private String displayName;

    public RegisterUserParams() {
        this.displayName = null;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
