package gameMechanics;

import base.Address;
import base.Frontend;
import messageSystem.MsgToFrontend;

public class MsgVictory extends MsgToFrontend {

    private Integer userId;
    private int count;
    private String victoryMsg;

    public MsgVictory(Address from, Address to, int userId, int count, String victoryMsg) {
        super(from, to);
        this.count = count;
        this.userId = userId;
        this.victoryMsg = victoryMsg;
    }

    public void exec(Frontend frontend) {
        frontend.setResult(userId, count, victoryMsg);

    }

}
