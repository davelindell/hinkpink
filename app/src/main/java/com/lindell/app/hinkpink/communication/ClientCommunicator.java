package com.lindell.app.hinkpink.communication;

import com.lindell.app.hinkpink.shared.ClientException;
import com.lindell.app.hinkpink.shared.communication.AddConnectionParams;
import com.lindell.app.hinkpink.shared.communication.ApproveConnectionParams;
import com.lindell.app.hinkpink.shared.communication.ApproveConnectionResult;
import com.lindell.app.hinkpink.shared.communication.GetConnectionGamesParams;
import com.lindell.app.hinkpink.shared.communication.GetConnectionGamesResult;
import com.lindell.app.hinkpink.shared.communication.GetGameParams;
import com.lindell.app.hinkpink.shared.communication.GetGameResult;
import com.lindell.app.hinkpink.shared.communication.GetUserConnectionsResult;
import com.lindell.app.hinkpink.shared.communication.NewGameParams;
import com.lindell.app.hinkpink.shared.communication.RegisterUserParams;
import com.lindell.app.hinkpink.shared.communication.SubmitGameInfoParams;
import com.lindell.app.hinkpink.shared.communication.SubmitGameInfoResult;
import com.lindell.app.hinkpink.shared.communication.ValidateUserParams;
import com.lindell.app.hinkpink.shared.communication.ValidateUserResult;
import com.lindell.app.hinkpink.shared.communication.AddConnectionResult;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lindell on 6/1/15.
 */
public class ClientCommunicator {
    private String host_url;
    private String port;

    public ClientCommunicator() {
//        this.host_url = "neon-idiom-635.appspot.com";
//        this.port = "80";
        this.host_url = "10.0.2.2";
        this.port = "8080";
    }

    /**
     * ValidateUser takes a wrapper class and returns a wrapper
     * with a User object inside of it. The method queries the server to see
     * if there is a valid user with the information provided in the parameter.
     *
     * @param params object of type ValidateUser_Params which is a wrapper for
     * strings which describe the user.
     * @return ValidateUser_Result, a wrapper class for a User, which is not null
     * if the validation was successful.
     * @throws ClientException
     */

    public SubmitGameInfoResult submitGameInfo(SubmitGameInfoParams params) throws ClientException {
        return (SubmitGameInfoResult)doPost("http://" + host_url + ":" + port + "/SubmitGameInfo", params);
    }

    public ValidateUserResult submitNewGame(NewGameParams params) throws ClientException {
        return (ValidateUserResult)doPost("http://" + host_url + ":" + port + "/SubmitNewGame", params);
    }

    public ApproveConnectionResult approveConnection(ApproveConnectionParams params) throws ClientException {
        return (ApproveConnectionResult)doPost("http://" + host_url + ":" + port + "/ApproveConnection", params);
    }

    public GetConnectionGamesResult getConnectionGames(GetConnectionGamesParams params) throws ClientException {
        return (GetConnectionGamesResult)doPost("http://" + host_url + ":" + port + "/GetConnectionGames", params);
    }

    public AddConnectionResult addFriend(AddConnectionParams params) throws ClientException {
        return (AddConnectionResult)doPost("http://" + host_url + ":" + port + "/AddFriend", params);
    }

    public ValidateUserResult validateUser(ValidateUserParams params) throws ClientException {
        return (ValidateUserResult)doPost("http://" + host_url + ":" + port + "/ValidateUser", params);
    }

    public ValidateUserResult registerUser(RegisterUserParams params) throws ClientException {
        return (ValidateUserResult)doPost("http://" + host_url + ":" + port + "/RegisterUser", params);
    }

    public GetUserConnectionsResult getUserConnections(ValidateUserParams params) throws ClientException {
        return (GetUserConnectionsResult)doPost("http://" + host_url + ":" + port + "/GetUserConnections", params);
    }

    public GetUserConnectionsResult getUserIncomingConnections(ValidateUserParams params) throws ClientException {
        return (GetUserConnectionsResult)doPost("http://" + host_url + ":" + port + "/GetUserIncomingConnections", params);
    }
    public GetGameResult getGame(GetGameParams params) throws ClientException {
        return (GetGameResult)doPost("http://" + host_url + ":" + port + "/GetGame", params);
    }

    private byte[] doGet(String urlPath) throws ClientException {
        // Make HTTP GET request to the specified URL,
        // and return the object returned by the server
        byte[] result = null;

        try {
            URL url = new URL(urlPath);

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("GET");

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream response_body = connection.getInputStream();
                // Read response body from InputStream ...

                result = IOUtils.toByteArray(response_body);
            }
            else {
                // SERVER RETURNED AN HTTP ERROR
                throw new ClientException();
            }
        }
        catch (IOException e) {
            // IO ERROR
            throw new ClientException();
        }

        return result;
    }

    private Object doPost(String urlPath, Object postData) throws ClientException {
        // Make HTTP POST request to the specified URL,
        // passing in the specified postData object
        XStream xml_stream = new XStream(new DomDriver());
        Object result_obj = null;


        try {
            URL url = new URL(urlPath);

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();
            OutputStream requestBody = connection.getOutputStream();

            // Write request body to OutputStream ...
            xml_stream.toXML(postData, requestBody);

            requestBody.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream responseBody = connection.getInputStream();
                // Read response body from InputStream ...
                result_obj = xml_stream.fromXML(responseBody);
                connection.disconnect();
            }
            else {
                // SERVER RETURNED AN HTTP ERROR
                throw new ClientException();
            }
        }
        catch (IOException e) {
            // IO ERROR
            System.out.println(e.getMessage());
            throw new ClientException();
        }

        return result_obj;
    }


}






