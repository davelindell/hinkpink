package com.lindell.app.hinkpink.shared.communication;

import com.lindell.app.hinkpink.shared.models.HinkPinkConnection;
import com.lindell.app.hinkpink.shared.models.HinkPinkUser;

/**
 * Created by lindell on 8/8/15.
 */
public class ApproveConnectionParams extends CommunicatorParams {
    HinkPinkConnection approvedConnection;

    public ApproveConnectionParams() {
        approvedConnection = new HinkPinkConnection();
    }

    public HinkPinkConnection getApprovedConnection() {
        return approvedConnection;
    }

    public void setApprovedConnection(HinkPinkConnection approvedConnection) {
        this.approvedConnection = approvedConnection;
    }
}
