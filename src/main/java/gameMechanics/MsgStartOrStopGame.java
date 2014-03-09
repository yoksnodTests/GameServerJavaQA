package gameMechanics;


import base.Address;
import base.Frontend;
import messageSystem.MsgToFrontend;

public class MsgStartOrStopGame extends MsgToFrontend {

    private boolean isGameProcessed;

    public MsgStartOrStopGame(Address from, Address to, boolean isGameProcessed) {
        super(from, to);
        this.isGameProcessed = isGameProcessed;
    }

    public void exec(Frontend frontend) {
        frontend.setIsGameProcessed(isGameProcessed);
    }

}
