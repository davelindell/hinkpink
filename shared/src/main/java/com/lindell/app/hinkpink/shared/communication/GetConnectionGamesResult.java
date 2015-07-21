package com.lindell.app.hinkpink.shared.communication;

import com.lindell.app.hinkpink.shared.models.HinkPinkConnection;
import com.lindell.app.hinkpink.shared.models.HinkPinkGame;

import java.util.List;

/**
 * Created by lindell on 7/8/15.
 */
public class GetConnectionGamesResult {
    private List<HinkPinkGame> gameList;

    public GetConnectionGamesResult() {
        this.gameList = null;
    }

    public List<HinkPinkGame> getGameList() {
        return gameList;
    }

    public void setGameList(List<HinkPinkGame> gameList) {
        this.gameList = gameList;
    }
}
