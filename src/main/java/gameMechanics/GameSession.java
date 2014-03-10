package gameMechanics;

public class GameSession {

    private final int id;
    private int countClicks = 0;

    public GameSession(int userId) {
        this.id = userId;
    }

    public int hashCode() {
        return id;
    }

    public int getCountClicks() {
        return countClicks;
    }

    public void setCountClicks(int countClicks) {
        this.countClicks = countClicks;
    }


}
