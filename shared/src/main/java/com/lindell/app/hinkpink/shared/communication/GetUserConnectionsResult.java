package com.lindell.app.hinkpink.shared.communication;

import com.lindell.app.hinkpink.shared.models.HinkPinkConnection;

import java.util.List;

/**
 * Created by lindell on 6/16/15.
 */
public class GetUserConnectionsResult {
    private List<HinkPinkConnection> connectionList;

    public GetUserConnectionsResult() {
        this.connectionList = null;
    }


    public List<HinkPinkConnection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<HinkPinkConnection> connectionList) {
        this.connectionList = connectionList;
    }
}
