package base;


public interface AccountService extends Abonent {

    public MessageSystem getMessageSystem();

    public Object initGamer(String userName, Integer sessionId);

    public void saveResult(int id, int countClicks);
}
