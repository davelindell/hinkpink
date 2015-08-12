package com.lindell.app.hinkpink.shared.communication;

import com.lindell.app.hinkpink.shared.models.HinkPinkUser;

/**
 * Created by lindell on 7/20/15.
 */
public class AddConnectionResult {
    private boolean valid;
    private boolean alreadyExists;

    /**
     */
    public AddConnectionResult() {
        this.valid = false;
        this.alreadyExists = false;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isAlreadyExists() {
        return alreadyExists;
    }

    public void setAlreadyExists(boolean alreadyExists) {
        this.alreadyExists = alreadyExists;
    }
}