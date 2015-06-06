package com.lindell.app.hinkpink.backend.server.facade;

/**
 * Created by lindell on 6/1/15.
 */
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.lindell.app.hinkpink.backend.shared.communication.ValidateUserParams;
import com.lindell.app.hinkpink.backend.shared.communication.ValidateUserResult;
import com.lindell.app.hinkpink.backend.shared.models.HinkPinkUser;
;

public class ServerFacade {
    private int port;
    private Objectify ofy;

    public static void initialize() {
        ObjectifyService.register(HinkPinkUser.class);
    }

    public ServerFacade() {
        this.port = 8080;
        this.ofy = ObjectifyService.ofy();
    }

    public ValidateUserResult validateUser(ValidateUserParams params) {
        HinkPinkUser fetched_user = ofy.load().type(HinkPinkUser.class).filter("email", params.getEmail()).first().now();
        HinkPinkUser user = new HinkPinkUser(fetched_user.getEmail(),fetched_user.getPassword());
        ValidateUserResult result = new ValidateUserResult(user,true);
        return result;
    }
}