package frontend;


import base.Msg;
import gameMechanics.GameMechanicImpl;
import gameMechanics.MsgSetGamerNames;
import gameMechanics.MsgUpdateClicks;


import java.io.IOException;


import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import java.util.concurrent.atomic.AtomicInteger;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import databaseService.DatabaseServiceImpl;

import resourceSystem.GameSessionResource;

import base.Address;
import base.Frontend;
import base.MessageSystem;


import utils.PageGenerator;
import utils.TimeHelper;

public class FrontendImpl extends AbstractHandler implements Runnable, Frontend {

    private static final String URL_GET_PARAM_NAME = "userName";
    private static final String URL_GET_PARAM_ID = "sessionId";
    private static final String HELLO = "Hello, gamer.";
    private static final String YOUR_ID = " Your id : ";
    private static final String TAG = "<h1>";
    private static final String FIRST_VISIT = "You came here for the first time";
    private static final String LAST_VISIT = "Last visit";
    private static final String DATE_TEMPLATE = "dd-MM-yyyy hh:mm:ss";
    private static final String TOP_RESULT = "Your best result : ";
    private static final String CLICKS = "clicks.";
    private static final String WAIT = "Wait authentication.";
    private static final String GAME_IS_STARTED = "Game is started";
    private static final String TOTAL_RESULT = "Your result";
    private static final String ENEMY = "Your enemy with number : ";
    private static final String DOING = " doing ";

    private final static AtomicInteger sessionIdGenerator = new AtomicInteger();
    private final Map<Integer, UserSession> userSessions = new HashMap<>();
    private Address address;
    private MessageSystem ms;
    private Integer userId;
    private UserSession userSession;
    private GameSessionResource resource;
    private boolean isGameProcessed;
    private boolean isGameFinished;


    public FrontendImpl(MessageSystem ms, GameSessionResource resource) throws IOException {
        this.ms = ms;
        this.resource = resource;
        address = new Address();
        ms.addService(this);
    }

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        PageGenerator pg = new PageGenerator();
        final int sessionId = sessionIdGenerator.incrementAndGet();
        pg.refreshHtmlPage(sessionId);
        response.getWriter().print(pg.getPage());
        final String name = request.getParameter(URL_GET_PARAM_NAME);
        final Integer id = request.getParameter(URL_GET_PARAM_ID) != null
                ? Integer.valueOf(request.getParameter(URL_GET_PARAM_ID))
                : null;

        UserSession session = userSessions.get(id);
        if (name != null && !name.isEmpty() && session == null) {
            session = new UserSession(id);
            userSession = session;
            userSession.setUserName(name);
            userSessions.put(userSession.hashCode(), userSession);

        }
        if (id != null && name != null && !name.isEmpty()) {
            UserSession newSession = userSessions.get(Integer.valueOf(id));
            userId = newSession.getUserId();
        }

        if (userId != null) {
            PrintWriter writer = response.getWriter();
            UserSession us = userSessions.get(Integer.valueOf(request.getParameter(URL_GET_PARAM_ID)));
            writer.print(TAG + HELLO + us.getUserName() + YOUR_ID +
                    +userId + TAG);
            if (us.getLastVisit() == null) {
                writer.print(FIRST_VISIT);
            } else {
                Date date = us.getLastVisit().getTime();
                writer.print(TAG + LAST_VISIT + TAG + createFormattedDate(date));
                writer.print(TAG + TOP_RESULT + us.getBestCountClicks() + CLICKS + TAG);
            }
            userId = null;
        } else {
            if (name == null || name.isEmpty() && isGameFinished) {
                return;
            }

            if (!isGameFinished) {
                PrintWriter writer = response.getWriter();
                writer.print(TAG + WAIT + TAG);
                if (session.isInProgress()) {
                    session.setInProgress(true);
                    Address address = getAddress();
                    Address to = ms.getAddressService().getAddress(DatabaseServiceImpl.class);
                    final int hashCode = userSession.hashCode();
                    Msg msg = new MsgGetUserId(address, to, name, hashCode);
                    ms.sendMessage(msg);

                }
            }
        }
        if (isAuthenticatedTwoUsers()){
            Address addr = getAddress();
            Address to = ms.getAddressService().getAddress(GameMechanicImpl.class);
            Collection<UserSession> sessions = userSessions.values();
            Msg msg = new MsgSetGamerNames(addr, to, sessions);
            ms.sendMessage(msg);
        }

        onStart(request, response);
        if (session.getUserId() != null) {
            onHandle(request, response);
        }
        onStop(request, response);
    }

    private static String createFormattedDate(Date date){
        return new SimpleDateFormat(DATE_TEMPLATE).format(date);
    }

    public void run() {
        while (true) {
            ms.execForAbonent(this);
            TimeHelper.sleep(100);
        }
    }

    public Address getAddress() {
        return address;
    }

    public MessageSystem getMessageSystem() {
        return ms;
    }

    public void updateUserId(int sessionId, int userId, int clickTopResult, Calendar lastVisitDate) {
        userSessions.get(sessionId).setUserId(userId);
        userSessions.get(sessionId).setLastVisit(lastVisitDate);
        userSessions.get(sessionId).setBestCountClicks(clickTopResult);
    }

    public boolean isAuthenticatedTwoUsers() {
        if (isGameProcessed || isGameFinished){
            return false;
        }
        if (userSessions.size() < resource.getCountGamers()) {
            return false;
        }
        for (UserSession userSession : userSessions.values()) {
            if (userSession.getUserId() == null) {
                return false;
            }
        }
        return true;
    }


    public void onHandle(HttpServletRequest request, HttpServletResponse response) {
        if (isGameFinished){
            return;
        }
        final int clicks = request.getParameter("param") != null
                ? Integer.valueOf(request.getParameter("param"))
                : -1;
        final int id = Integer.valueOf(request.getParameter("sessionId"));
        UserSession session = userSessions.get(id);
        if (clicks > session.getClicks()) {
            session.setClicks(clicks);
            Address to = ms.getAddressService().getAddress(GameMechanicImpl.class);
            final int userId = session.getUserId();
            Msg msg = new MsgUpdateClicks(this.address, to, userId, clicks);
            ms.sendMessage(msg);
        }
    }

    public void onStop(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final int id = Integer.valueOf(request.getParameter("sessionId"));
        UserSession session = userSessions.get(id);
        if ((isGameProcessed && session.isWinner()) && !isGameFinished){
            return;
        }
        PrintWriter printWriter = response.getWriter();
        printWriter.println(TOTAL_RESULT + session.getClicks());
        for (UserSession each : userSessions.values()) {
            if (each.hashCode() != Integer.valueOf(id)){
                printWriter.println(TAG + TAG);
                printWriter.println(ENEMY + each.getUserId()
                        + " " + each.getUserName() +
                        DOING + each.getClicks() + CLICKS);
            }
        }
        if (session.getVictoryMsg() != null) {
            printWriter.println(TAG + TAG);
            printWriter.println(session.getVictoryMsg());
        }
    }

    public void setResult(Integer userId, int count, String victoryMessage) {
        if (!isGameProcessed) return;

        for (UserSession userSession : userSessions.values()) {
            if (userSession.getUserId() == userId) {
                userSession.setVictoryMsg(victoryMessage);
                userSession.setWinner(true);
            }

        }
        isGameFinished = true;
    }

    public void onStart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isGameFinished){
            return;
        }

        String id = request.getParameter(URL_GET_PARAM_ID);
        UserSession session = userSessions.get(String.valueOf(id));
        if (isGameProcessed() && !session.isWinner()){
            response.getWriter().print(GAME_IS_STARTED);
        }else{
            return;
        }

    }

    public boolean isGameProcessed() {
        return this.isGameProcessed;
    }

    public void setIsGameProcessed(boolean gameActivity) {
        this.isGameProcessed = gameActivity;
    }

    public Map<Integer, UserSession> getUserSessions() {
        return userSessions;
    }
}