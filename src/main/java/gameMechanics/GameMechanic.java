package gameMechanics;

import java.util.List;

import base.Abonent;

public interface GameMechanic extends Abonent {
    void sendStartGameMsg(List<Integer> userIds);

    void processMessages(int userId, int countClicks);
}
