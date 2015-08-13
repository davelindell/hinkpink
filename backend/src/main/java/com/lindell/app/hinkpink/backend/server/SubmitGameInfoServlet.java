package com.lindell.app.hinkpink.backend.server;

import com.lindell.app.hinkpink.backend.server.facade.ServerFacade;
import com.lindell.app.hinkpink.shared.communication.RegisterUserParams;
import com.lindell.app.hinkpink.shared.communication.SubmitGameInfoParams;
import com.lindell.app.hinkpink.shared.communication.ValidateUserParams;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by lindell on 6/13/15.
 */
public class SubmitGameInfoServlet extends HttpServlet {

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

        SubmitGameInfoParams params =
                (SubmitGameInfoParams)xml_stream.fromXML(req_body.toString());

        Object result = null;

        result = (Object)facade.submitGameInfo(params);
        String resp_xml = xml_stream.toXML(result);


        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(resp_xml);
    }
}
