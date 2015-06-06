package com.lindell.app.hinkpink.backend.shared.communication;

import com.lindell.app.hinkpink.backend.shared.models.HinkPinkUser;

/**
 * Created by lindell on 6/1/15.
 */
public class ValidateUserResult {
    private HinkPinkUser user;
    private boolean valid;

    /**
     * @param user User object stored in this wrapper class
     * @param valid flag to signify that this object contains a valid user
     */
    public ValidateUserResult(HinkPinkUser user, boolean valid) {
        this.user = user;
        this.valid = valid;
    }

    @Override
    public String toString() {
        if (valid) {
            return "TRUE\n" + user.getEmail() + "\n" + user.getPassword() + "\n";
        }
        else
            return "FALSE\n";
    }

    public HinkPinkUser getUser() {
        return user;
    }

    public void setUser(HinkPinkUser user) {
        this.user = user;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

}