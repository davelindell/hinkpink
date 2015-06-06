package com.lindell.app.hinkpink.backend.server;

/**
 * Created by lindell on 6/1/15.
 */
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import com.lindell.app.hinkpink.backend.server.facade.ServerFacade;
import com.lindell.app.hinkpink.backend.shared.communication.ValidateUserParams;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class ValidateUserHandler implements HttpHandler {

    private Logger logger = Logger.getLogger("record_server");

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        ServerFacade facade = new ServerFacade();
        XStream xml_stream = new XStream(new DomDriver());
        BufferedInputStream bis = new BufferedInputStream(exchange.getRequestBody());
        ValidateUserParams params = (ValidateUserParams)xml_stream.fromXML(bis);
        bis.close();
        Object result = null;

        result = (Object)facade.validateUser(params);
        exchange.sendResponseHeaders(200, 0);

        OutputStream os = exchange.getResponseBody();

        xml_stream.toXML(result, os);

        os.close();
    }
}