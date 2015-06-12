package com.lindell.app.hinkpink.backend.server;

/**
 * Created by lindell on 6/5/15.
 */

import com.googlecode.objectify.ObjectifyService;
import static com.googlecode.objectify.ObjectifyService.ofy;
import com.lindell.app.hinkpink.backend.server.facade.ServerFacade;
import com.lindell.app.hinkpink.shared.communication.ValidateUserParams;
import com.lindell.app.hinkpink.shared.communication.ValidateUserResult;
import com.lindell.app.hinkpink.shared.models.HinkPinkUser;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.*;

public class ValidateUserServlet extends HttpServlet {


    public ValidateUserServlet(){
        ObjectifyService.register(HinkPinkUser.class);
        HinkPinkUser fetched_user = ofy().load().type(HinkPinkUser.class).filter("email", "t@t.com").first().now();
        if (fetched_user == null) {
            fetched_user = new HinkPinkUser();
            fetched_user.setEmail("t@t.com");
            fetched_user.setPassword("password");
            ofy().save().entity(fetched_user).now();
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        ServerFacade facade = new ServerFacade();
        XStream xml_stream = new XStream(new DomDriver());
        String line = null;
        StringBuffer req_body = new StringBuffer();

        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                req_body.append(line);
        } catch (Exception e) {System.out.print(e.getMessage());}

        ValidateUserParams params =
                (ValidateUserParams)xml_stream.fromXML(req_body.toString());

        Object result = null;

        result = (Object)facade.validateUser(params);
        String resp_xml = xml_stream.toXML(result);


        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(resp_xml);
    }
}

