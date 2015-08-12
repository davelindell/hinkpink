package com.lindell.app.hinkpink.shared.communication;

import com.lindell.app.hinkpink.shared.models.HinkPinkConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lindell on 8/8/15.
 */
public class ApproveConnectionResult {

    private List<HinkPinkConnection> connectionList;
    private List<HinkPinkConnection> incomingConnectionList;
    public ApproveConnectionResult() {
        connectionList = new ArrayList<HinkPinkConnection>();
        incomingConnectionList = new ArrayList<HinkPinkConnection>();
    }

    public List<HinkPinkConnection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<HinkPinkConnection> connectionList) {
        this.connectionList = connectionList;
    }

    public List<HinkPinkConnection> getIncomingConnectionList() {
        return incomingConnectionList;
    }

    public void setIncomingConnectionList(List<HinkPinkConnection> incomingConnectionList) {
        this.incomingConnectionList = incomingConnectionList;
    }
}
