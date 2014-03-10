package frontend;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import databaseService.DatabaseServiceImpl;
import gameMechanics.GameMechanic;
import gameMechanics.GameMechanicImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import messageSystem.MessageSystemImpl;

import org.eclipse.jetty.server.Request;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import resourceSystem.DatabaseResource;
import resourceSystem.GameSessionResource;
import base.MessageSystem;
import resourceSystem.ResourceFactory;

public class FrontendTest {

    private static final String GAME_RES = "GameResource.xml";
    private static final String DB_RES = "DatabaseResource.xml";

    @Test
    public void testServlets() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DOMException, NoSuchFieldException, SecurityException, IllegalArgumentException, ParserConfigurationException, SAXException, IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Request baseRequest = mock(Request.class);
        ResourceFactory factory = ResourceFactory.getInstance();
        MessageSystem ms = new MessageSystemImpl();
        GameMechanic gameMechanic = new GameMechanicImpl(ms,
                (GameSessionResource) factory.get(GAME_RES));
        ms.addService(gameMechanic);
        FrontendImpl frontendImpl = new FrontendImpl(ms,
                (GameSessionResource) factory.get(GAME_RES));
        ms.addService(frontendImpl);
        when(request.getParameter("userName")).thenReturn("donskoy");
        when(request.getParameter("sessionId")).thenReturn("100500");
        when(request.getParameter("param")).thenReturn("20");
        assertNotNull(request.getParameter("param"));
        PrintWriter writer = new PrintWriter("somefile.txt");
        when(response.getWriter()).thenReturn(writer);
        UserSession session = new UserSession(100500);
        session.setUserId(100500);
        session.setWinner(false);
        session.setClicks(10);
        session.setBestCountClicks(10);
        session.setVictoryMsg("Winner");
        session.setLastVisit(Calendar.getInstance());


        DatabaseServiceImpl databaseServiceImpl = new DatabaseServiceImpl(ms,(DatabaseResource) factory.get("DatabaseResour�e.xml") );
        ms.addService(databaseServiceImpl);
        frontendImpl.getUserSessions().put(100500, session);
		frontendImpl.updateUserId(100500, 100501, 120, Calendar.getInstance());
		frontendImpl.handle("UTF-8", baseRequest, request, response);
		frontendImpl.onHandle(request, response);
		frontendImpl.setIsGameProcessed(true);
		frontendImpl.setResult(1, 1000, "winner");
		frontendImpl.onStop(request, response);
		frontendImpl.onStart(request, response);
        writer.flush();
        Assert.assertNotNull(frontendImpl.getUserSessions().get(100500));
    }


    @Test
    public void firstGameFrontend() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DOMException, NoSuchFieldException, SecurityException, IllegalArgumentException, ParserConfigurationException, SAXException, IOException, ServletException{
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Request baseRequest = mock(Request.class);
        ResourceFactory factory = ResourceFactory.getInstance();
        MessageSystem ms = new MessageSystemImpl();
        GameMechanic gameMechanic = new GameMechanicImpl(ms,
                (GameSessionResource) factory.get(GAME_RES));
        ms.addService(gameMechanic);
        FrontendImpl frontendImpl = new FrontendImpl(ms,
                (GameSessionResource) factory.get(GAME_RES));
        ms.addService(frontendImpl);
        when(request.getParameter("userName")).thenReturn("james");
        when(request.getParameter("sessionId")).thenReturn("100500");
        when(request.getParameter("param")).thenReturn("20");
        Assert.assertNotNull(request.getParameter("param"));
        PrintWriter writer = new PrintWriter("somefile.txt");
        when(response.getWriter()).thenReturn(writer);
        UserSession session = new UserSession(100500);
        session.setUserId(100500);
        session.setWinner(false);
        session.setClicks(10);
        session.setBestCountClicks(10);
        session.setVictoryMsg("Winner");
        frontendImpl.getUserSessions().clear();
        DatabaseServiceImpl databaseServiceImpl = new DatabaseServiceImpl(ms,(DatabaseResource) factory.get(DB_RES) );
        ms.addService(databaseServiceImpl);
        frontendImpl.handle("UTF-8", baseRequest, request, response);

        writer.flush();

        Assert.assertNotNull(frontendImpl.getUserSessions().get(100500));
    }
}
