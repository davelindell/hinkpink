package com.lindell.app.hinkpink.backend.server.facade;

/**
 * Created by lindell on 6/1/15.
 */
import com.lindell.app.hinkpink.shared.communication.ValidateUserParams;
import com.lindell.app.hinkpink.shared.communication.ValidateUserResult;
import com.lindell.app.hinkpink.shared.models.HinkPinkUser;
import static com.googlecode.objectify.ObjectifyService.ofy;


public class ServerFacade {

    public ValidateUserResult validateUser(ValidateUserParams params) {
        HinkPinkUser fetched_user = ofy().load().type(HinkPinkUser.class).filter("email", params.getEmail()).first().now();
        ValidateUserResult result = null;

        if (fetched_user == null) {
            result = new ValidateUserResult();
            result.setUser(new HinkPinkUser());
        }
        else {
            HinkPinkUser user = new HinkPinkUser();
            user.setEmail(fetched_user.getEmail());
            user.setPassword(fetched_user.getPassword());
            result = new ValidateUserResult();
            result.setUser(user);
            result.setValid(true);
        }

        return result;
    }
}