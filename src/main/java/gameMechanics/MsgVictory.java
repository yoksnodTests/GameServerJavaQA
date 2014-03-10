package gameMechanics;

import base.Address;
import base.Frontend;
import messageSystem.MsgToFrontend;

public class MsgVictory extends MsgToFrontend {

    private Integer sessionId;
    private int count;
    private String victoryMsg;

    public MsgVictory(Address from, Address to, int sessionId, int count, String victoryMsg) {
        super(from, to);
        this.count = count;
        this.sessionId = sessionId;
        this.victoryMsg = victoryMsg;
    }

    public void exec(Frontend frontend) {
        frontend.setResult(sessionId, count, victoryMsg);

    }

}
