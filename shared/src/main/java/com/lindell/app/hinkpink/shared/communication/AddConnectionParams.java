package com.lindell.app.hinkpink.shared.communication;

/**
 * Created by lindell on 7/3/15.
 */
public class AddConnectionParams extends CommunicatorParams {

    /**
     * The constructor for the ValidateUser_Params class
     */
    private String friendEmail;
    private String friendDisplayName;
    public AddConnectionParams() {

    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public void setFriendEmail(String friend_email) {
        this.friendEmail = friendEmail;
    }

    public String getFriendDisplayName() {
        return friendDisplayName;
    }

    public void setFriendDisplayName(String friendDisplayName) {
        this.friendDisplayName = friendDisplayName;
    }
}
