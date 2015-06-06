package com.lindell.app.hinkpink.backend.server;

/**
 * Created by lindell on 6/5/15.
 */

import com.googlecode.objectify.ObjectifyService;
import com.lindell.app.hinkpink.backend.shared.models.HinkPinkUser;

import java.io.IOException;
import javax.servlet.http.*;

public class Servlet extends HttpServlet {


    public Servlet(){
        ObjectifyService.register(HinkPinkUser.class);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("text/plain");
        resp.getWriter().println("Please use the form to POST to this url");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String name = req.getParameter("name");
        resp.setContentType("text/plain");
        if (name == null) {
            resp.getWriter().println("Please enter a name");
        }
        resp.getWriter().println("Hello " + name);
    }
}

