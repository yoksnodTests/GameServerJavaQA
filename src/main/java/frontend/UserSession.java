package frontend;


import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;


public class UserSession {
	
	private Integer sessionId ;
	private String userName;
	private Integer userId;
	private boolean inProgress = false;
	private int clicks;
	private boolean winner;
	private String victoryMsg;
	private Calendar lastVisit;
	private int bestCountClicks;

	public UserSession( int value){
		this.sessionId = value;
	}
	public int hashCode() {
		return sessionId;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public boolean isInProgress() {
		return inProgress;
	}
	public void setInProgress(boolean inProgress) {
		this.inProgress = inProgress;
	}
	public int getClicks() {
		return clicks;
	}
	public void setClicks(int clicks) {
		this.clicks = clicks;
	}
	public boolean isWinner() {
		return winner;
	}
	public void setWinner(boolean winner) {
		this.winner = winner;
	}
	public String getVictoryMsg() {
		return victoryMsg;
	}
	public void setVictoryMsg(String victoryMsg) {
		this.victoryMsg = victoryMsg;
	}
	public Calendar getLastVisit() {
		return lastVisit;
	}
	public void setLastVisit(Calendar lastVisit) {
		this.lastVisit = lastVisit;
	}
	public int getBestCountClicks() {
		return bestCountClicks;
	}
	public void setBestCountClicks(int bestCountClicks) {
		this.bestCountClicks = bestCountClicks;
	}
}
