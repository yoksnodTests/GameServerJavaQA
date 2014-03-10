package gameMechanics;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.Msg;
import com.google.common.annotations.VisibleForTesting;
import databaseService.DatabaseServiceImpl;

import resourceSystem.GameSessionResource;

import frontend.FrontendImpl;


import base.Address;
import base.MessageSystem;
import utils.TimeHelper;


public class GameMechanicImpl implements Runnable, GameMechanic {

    private static final int DEFAULT_GAME_TIMEOUT = 1;

    private static final String GAME_OVER = "The game is over";
    private static final String YOU_LOSE = "You lose";
    private static final String YOU_WIN = "You win";

	private MessageSystem ms;
	private GameSessionResource resource;
	private Address address;
	private boolean isGameAlive;
	private long gameTime = DEFAULT_GAME_TIMEOUT;
	private final Map<Integer, GameSession> userSessions = new HashMap();

	
	public GameMechanicImpl (MessageSystem ms , GameSessionResource resource){
		this.ms = ms;
        this.resource = resource;
        address = new Address();
		ms.addService(this);
	}
	public void run()  {		
		while (true){			
			ms.execForAbonent(this);
			doGameMechanicStep();
			TimeHelper.sleep(100);
		}		
}

	public Address getAddress() {	
		return address;
	}
	public MessageSystem getMessageSystem(){
		return ms;
	}
	public void processMessages(int userId, int countClicks){		
		getSessions().get(userId).setCountClicks(countClicks);
	}

	public void doGameMechanicStep(){
					
		if ( resource == null ){
			return;
		}

		if (System.currentTimeMillis() - gameTime >= resource.getTime() && ((gameTime !=0) && isGameAlive)) {
			isGameAlive = false;
            Comparator<GameSession> comparator = new GameSessionComparator();
			GameSession bestSession = Collections.max(userSessions.values(), comparator);
            Address to = ms.getAddressService().getAddress(FrontendImpl.class);
            final int bestSessionId = bestSession.hashCode();
            final int topResult = bestSession.getCountClicks();
            Msg victoryMsg = new MsgVictory(address, to, bestSessionId, topResult, GAME_OVER + YOU_WIN);
			ms.sendMessage(victoryMsg);
            checkLoseGamers(bestSession);

            Msg startGameMsg = new MsgStartOrStopGame(address, to, false);
			ms.sendMessage(startGameMsg);
            saveResults();
		}
	}

    private void saveResults(){
        for (GameSession each : userSessions.values()){
            Address to = ms.getAddressService().getAddress(DatabaseServiceImpl.class);
            final int sessionId = each.hashCode();
            final int result = each.getCountClicks();
            Msg saveMsg = new MsgSaveResult(address, to, sessionId, result);
            ms.sendMessage(saveMsg);
        }
    }

    private void checkLoseGamers(GameSession bestSession){
        for (GameSession badSession : userSessions.values()) {
            if (!badSession.equals(bestSession)){
                Address to =  ms.getAddressService().getAddress(FrontendImpl.class);
                final int sessionId = badSession.hashCode();
                final int topResult = badSession.getCountClicks();

                Msg msg = new MsgVictory(address, to, sessionId, topResult, GAME_OVER + YOU_LOSE);
                ms.sendMessage(msg);
            }
        }
    }

    public void sendStartGameMsg(List<Integer> userIds){
        initSessions(userIds);
		if ( userSessions.size() == resource.getCountGamers() ) {
            gameTime = System.currentTimeMillis();
        }

		isGameAlive = true;
        Address to = ms.getAddressService().getAddress(FrontendImpl.class);
		Msg msg = new MsgStartOrStopGame(address, to, true);
        ms.sendMessage(msg);
	}

    public void initSessions(List<Integer> userIds){
        for (Integer id : userIds) {
            userSessions.put(id, new GameSession(id));
        }
    }

	public Map<Integer, GameSession> getSessions(){
		return userSessions;
	}

    private static class GameSessionComparator implements Comparator<GameSession>{

        public boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override
        public int compare(GameSession o1, GameSession o2) {
            if (o1.getCountClicks() > o2.getCountClicks()) {
                return 1;
            }

            if (o1.getCountClicks() < o2.getCountClicks()){
                return -1;
            }

            return 0;
        }
    }
}
