package com.lindell.app.hinkpink.backend.shared.communication;

/**
 * Created by lindell on 6/1/15.
 */
public class ValidateUserParams extends CommunicatorParams {

    /**
     * The constructor for the ValidateUser_Params class
     * @param email the user's email
     * @param password the user's password
     */
    public ValidateUserParams(String email, String password) {
        super(email, password);
    }
}
