package com.lindell.app.hinkpink.backend.server;


import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.lindell.app.hinkpink.shared.models.HinkPinkConnection;
import com.lindell.app.hinkpink.shared.models.HinkPinkGame;
import com.lindell.app.hinkpink.shared.models.HinkPinkUser;


public class OfyService {
    static {
        ObjectifyService.register(HinkPinkUser.class);
        ObjectifyService.register(HinkPinkConnection.class);
        ObjectifyService.register(HinkPinkGame.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }

}