package com.lindell.app.hinkpink.shared.communication;

import com.lindell.app.hinkpink.shared.models.HinkPinkGame;

/**
 * Created by lindell on 8/12/15.
 */
public class GetGameResult {

    HinkPinkGame game;

    public GetGameResult() {
        game = null;
    }

    public HinkPinkGame getGame() {
        return game;
    }

    public void setGame(HinkPinkGame game) {
        this.game = game;
    }
}
