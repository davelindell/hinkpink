package com.lindell.app.hinkpink.shared.communication;

import com.lindell.app.hinkpink.shared.models.HinkPinkUser;

/**
 * Created by lindell on 6/1/15.
 */
public class ValidateUserResult {
    private HinkPinkUser user;
    private boolean valid;

    /**
     */
    public ValidateUserResult() {
        this.user = null;
        this.valid = false;
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