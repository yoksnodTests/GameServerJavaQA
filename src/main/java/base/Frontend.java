package base;

import java.util.Calendar;


public interface Frontend extends Abonent {

    MessageSystem getMessageSystem();

    void updateUserId(int sessionId, int userId, int clickTopResult, Calendar lastVisitDate);

    boolean isGameProcessed();

    void setIsGameProcessed(boolean gameStarted);

    void setResult(Integer userId, int count, String victoryMessage);

}
