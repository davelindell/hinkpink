package com.lindell.app.hinkpink.backend.server.facade;

/**
 * Created by lindell on 6/1/15.
 */
import com.googlecode.objectify.Key;
import com.lindell.app.hinkpink.backend.server.OfyService;
import com.lindell.app.hinkpink.shared.communication.AddConnectionResult;
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
import com.lindell.app.hinkpink.shared.communication.AddConnectionParams;
import com.lindell.app.hinkpink.shared.models.HinkPinkConnection;
import com.lindell.app.hinkpink.shared.models.HinkPinkGame;
import com.lindell.app.hinkpink.shared.models.HinkPinkUser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ServerFacade {

    public ValidateUserResult validateUser(ValidateUserParams params) {
        HinkPinkUser fetched_user = OfyService.ofy().load().type(HinkPinkUser.class).filter("email", params.getEmail()).first().now();
        ValidateUserResult result = null;

        if (fetched_user == null) {
            result = new ValidateUserResult();
            result.setUser(new HinkPinkUser());
        }
        else {
            if (fetched_user.getPassword().equals(params.getPassword())) {
                HinkPinkUser user = new HinkPinkUser();
                user.setEmail(fetched_user.getEmail());
                user.setPassword(fetched_user.getPassword());
                result = new ValidateUserResult();
                result.setUser(user);
                result.setValid(true);
            }
            else {
                result = new ValidateUserResult();
                result.setUser(new HinkPinkUser());
            }
        }

        return result;
    }

    public ValidateUserResult registerUser(RegisterUserParams params) {
        HinkPinkUser fetched_user = OfyService.ofy().load().type(HinkPinkUser.class).filter("email", params.getEmail()).first().now();
        ValidateUserResult result = null;

        // Then this account does not already exist, create it
        if (fetched_user == null) {
            // create the user and save to the datastore
            HinkPinkUser user = new HinkPinkUser();
            user.setDisplayName(params.getDisplayName());
            user.setEmail(params.getEmail());
            user.setPassword(params.getPassword());
            OfyService.ofy().save().entity(user).now();

            // Set the return value
            result = new ValidateUserResult();
            result.setUser(user);
            result.setValid(true);

        }
        // The accound already exists, return null result value
        else {
            result = new ValidateUserResult();
            result.setUser(new HinkPinkUser());
        }

        return result;
    }

    public GetUserConnectionsResult getUserConnections(ValidateUserParams params) {
        HinkPinkUser fetched_user = OfyService.ofy().load().type(HinkPinkUser.class).filter("email", params.getEmail()).first().now();
        GetUserConnectionsResult result = null;

        // Then this account does not already exist, then we return an empty object
        if (fetched_user == null) {
            // Set the return value
            result = new GetUserConnectionsResult();
        }
        // The account exists, check the password
        else {
            if (fetched_user.getPassword().equals(params.getPassword())) {
                result = new GetUserConnectionsResult();
                fetched_user.getConnectionList();

                // Get connection list from datastore
                if (!fetched_user.getConnectionList().isEmpty()) {
                    Map<Long,HinkPinkConnection> connections = OfyService.ofy().load().type(HinkPinkConnection.class).ids(fetched_user.getConnectionList());
                    result.setConnectionList(new LinkedList<HinkPinkConnection>(connections.values()));
                } else {
                    result.setConnectionList(new LinkedList<HinkPinkConnection>());
                }


            }
            else {
                result = new GetUserConnectionsResult();
            }
        }

        return result;
    }

    public ApproveConnectionResult approveConnection(ApproveConnectionParams params) {
        HinkPinkUser fetched_user = OfyService.ofy().load().type(HinkPinkUser.class).filter("email", params.getEmail()).first().now();
        HinkPinkUser request_user = OfyService.ofy().load().type(HinkPinkUser.class).filter("email", params.getApprovedConnection().getEmail()).first().now();
        ApproveConnectionResult result = null;

        // Then this account does not already exist, then we return an empty object
        if (fetched_user == null) {
            // Set the return value
            result = new ApproveConnectionResult();
        }
        // The account exists, check the password
        else {
            if (fetched_user.getPassword().equals(params.getPassword())) {
                result = new ApproveConnectionResult();
                Map<Long, HinkPinkConnection> connectionUpdate = null;
                Map<Long, HinkPinkConnection> incomingConnectionUpdate = null;

                // remove the connection from incoming list of the approver
                Iterator<Long> iter = fetched_user.getIncomingConnectionList().iterator();
                boolean found = false;
                while (iter.hasNext() && !found) {
                    if (iter.next().equals(params.getApprovedConnection().getId())) {
                        found = true;
                        iter.remove();
                    }
                }

                // remove from the pending list of the requester
                iter = request_user.getPendingConnectionList().iterator();
                found = false;
                while (iter.hasNext() && !found) {
                    if (iter.next().equals(params.getApprovedConnection().getPlayerConnectionID())) {
                        found = true;
                        iter.remove();
                    }
                }

                // transfer approvedConnection and pendingConnection to the approved connection lists for requester/approver
                fetched_user.getConnectionList().add(params.getApprovedConnection().getId());
                request_user.getConnectionList().add(params.getApprovedConnection().getPlayerConnectionID());

                // save
                OfyService.ofy().save().entity(fetched_user).now();
                OfyService.ofy().save().entity(request_user).now();


                connectionUpdate = OfyService.ofy().load().type(HinkPinkConnection.class).ids(fetched_user.getConnectionList());
                incomingConnectionUpdate = OfyService.ofy().load().type(HinkPinkConnection.class).ids(fetched_user.getIncomingConnectionList());

                result.setConnectionList(new LinkedList<HinkPinkConnection>(connectionUpdate.values()));
                result.setIncomingConnectionList(new LinkedList<HinkPinkConnection>(incomingConnectionUpdate.values()));
            } else {
                result = new ApproveConnectionResult();
            }
        }
        return result;

    }

    public GetUserConnectionsResult getUserIncomingConnections(ValidateUserParams params) {
        HinkPinkUser fetched_user = OfyService.ofy().load().type(HinkPinkUser.class).filter("email", params.getEmail()).first().now();
        GetUserConnectionsResult result = null;

        // Then this account does not already exist, then we return an empty object
        if (fetched_user == null) {
            // Set the return value
            result = new GetUserConnectionsResult();
        }
        // The account exists, check the password
        else {
            if (fetched_user.getPassword().equals(params.getPassword())) {
                result = new GetUserConnectionsResult();
                List<Long> conList = fetched_user.getIncomingConnectionList();
                Map<Long,HinkPinkConnection> connections = null;
                // Get connection list from datastore
                if (!fetched_user.getIncomingConnectionList().isEmpty()) {
                    connections = OfyService.ofy().load().type(HinkPinkConnection.class).ids(fetched_user.getIncomingConnectionList());
                    result.setConnectionList(new LinkedList<HinkPinkConnection>(connections.values()));
                } else {
                    result.setConnectionList(new LinkedList<HinkPinkConnection>());
                }
            }
            else {
                result = new GetUserConnectionsResult();
            }
        }

        return result;
    }

    public AddConnectionResult addConnection(AddConnectionParams params) {
        HinkPinkUser fetched_user = OfyService.ofy().load().type(HinkPinkUser.class).filter("email", params.getEmail()).first().now();
        HinkPinkUser friend_user = OfyService.ofy().load().type(HinkPinkUser.class).filter("email", params.getFriendEmail()).first().now();
        AddConnectionResult result = null;

        if (fetched_user == null || friend_user == null) {
            result = new AddConnectionResult();
        }
        else {
            if (fetched_user.getPassword().equals(params.getPassword())) {
                // check for duplicate entry
                Map<Long,HinkPinkConnection> connections = OfyService.ofy().load().type(HinkPinkConnection.class).ids(fetched_user.getConnectionList());
                Iterator<HinkPinkConnection> iter = connections.values().iterator();
                boolean found = false;
                while (iter.hasNext() && !found) {
                    if (iter.next().getEmail().equals(friend_user.getEmail())) {
                        found = true;
                    }
                }

                if (found) { // user already exists, return early
                    result = new AddConnectionResult();
                    result.setAlreadyExists(true);
                    return result;
                }

                // make a new connection
                HinkPinkConnection connection = new HinkPinkConnection();
                connection.setDisplayName(friend_user.getDisplayName());
                connection.setEmail(friend_user.getEmail());
                connection.setPlayerID(friend_user.getId());

                // save the connection and get the ID
                Key<HinkPinkConnection> key = OfyService.ofy().save().entity(connection).now();

                // Add the connection to the Pending list for the requesting user
                fetched_user.getPendingConnectionList().add(key.getId());

                // make another connection
                HinkPinkConnection incoming_connection = new HinkPinkConnection();
                incoming_connection.setDisplayName((fetched_user.getDisplayName()));
                incoming_connection.setEmail(fetched_user.getEmail());
                incoming_connection.setPlayerID(fetched_user.getId());

                // save the connection and get the ID
                Key<HinkPinkConnection> incoming_key = OfyService.ofy().save().entity(incoming_connection).now();

                // Add the connection to the incoming list for the potential friend
                friend_user.getIncomingConnectionList().add(incoming_key.getId());

                // add the newly minted Connection IDs to each other's connections and save
                connection.setPlayerConnectionID(incoming_key.getId());
                incoming_connection.setPlayerConnectionID(key.getId());
                OfyService.ofy().save().entity(connection).now();
                OfyService.ofy().save().entity(incoming_connection).now();

                // save the users' data
                OfyService.ofy().save().entity(fetched_user).now();
                OfyService.ofy().save().entity(friend_user).now();

                result = new AddConnectionResult();
                result.setValid(true);
            }
            else {
                result = new AddConnectionResult();
            }
        }

        return result;
    }

    public GetConnectionGamesResult getConnectionGames(GetConnectionGamesParams params) {
        HinkPinkUser fetched_user = OfyService.ofy().load().type(HinkPinkUser.class).filter("email", params.getEmail()).first().now();
        GetConnectionGamesResult result = new GetConnectionGamesResult();

        if (fetched_user != null) {
            if (fetched_user.getPassword().equals(params.getPassword())) {
                //fetch the games
                HinkPinkConnection connections = OfyService.ofy().load().type(HinkPinkConnection.class).id(params.getConnectionID()).now();
                if (connections != null) {
                    Map<Long,HinkPinkGame> games = OfyService.ofy().load().type(HinkPinkGame.class).ids(connections.getGameList());
                    result.setGameList(new ArrayList<HinkPinkGame>(games.values()));
                }
            }
        }

        return result;
    }

    public ValidateUserResult submitNewGame(NewGameParams params) {
        HinkPinkUser fetched_user = OfyService.ofy().load().type(HinkPinkUser.class).filter("email", params.getEmail()).first().now();
        ValidateUserResult result = new ValidateUserResult();

        if (fetched_user != null) {
            if (fetched_user.getPassword().equals(params.getPassword())) {
                //fetch the games
                HinkPinkConnection hinterConnection = OfyService.ofy().load().type(HinkPinkConnection.class).id(params.getConnectionID()).now();
                HinkPinkConnection guesserConnection = OfyService.ofy().load().type(HinkPinkConnection.class).id(params.getPlayerConnectionID()).now();

                // make new hinkpink game
                HinkPinkGame game = new HinkPinkGame();
                game.setGuessPlayerID(hinterConnection.getPlayerID());
                game.setHintPlayerID(guesserConnection.getPlayerID());
                game.setHint(params.getHint());
                game.setNumSyllables(params.getNumSyllables());

                // save game and get key
                Key<HinkPinkGame> key = OfyService.ofy().save().entity(game).now();

                // add game to both connections
                hinterConnection.getGameList().add(key.getId());
                guesserConnection.getGameList().add(key.getId());
                result.setValid(true);
            }
        }

        return result;
    }


    public SubmitGameInfoResult submitGameInfo(SubmitGameInfoParams params) {
        HinkPinkUser fetched_user = OfyService.ofy().load().type(HinkPinkUser.class).filter("email", params.getEmail()).first().now();
        SubmitGameInfoResult result = new SubmitGameInfoResult();
        result.setReceived(false);

        if (fetched_user != null) {
            if (fetched_user.getPassword().equals(params.getPassword())) {
                //fetch the games
                HinkPinkGame game = OfyService.ofy().load().type(HinkPinkGame.class).id(params.getGameID()).now();

                // add in info
                if (params.isGuess()) {
                    game.getGuesses().add(params.getInformation());
                } else {
                    game.getExtraHints().add(params.getInformation());
                }

                // save game and get key
                Key<HinkPinkGame> key = OfyService.ofy().save().entity(game).now();

                result.setReceived(true);
            }
        }

        return result;
    }

    public GetGameResult getGame(GetGameParams params) {
        HinkPinkUser fetched_user = OfyService.ofy().load().type(HinkPinkUser.class).filter("email", params.getEmail()).first().now();
        GetGameResult result = new GetGameResult();

        if (fetched_user != null) {
            if (fetched_user.getPassword().equals(params.getPassword())) {
                //fetch the games
                HinkPinkGame game = OfyService.ofy().load().type(HinkPinkGame.class).id(params.getGameID()).now();
                result.setGame(game);
            }
        }
        return result;
    }

}

