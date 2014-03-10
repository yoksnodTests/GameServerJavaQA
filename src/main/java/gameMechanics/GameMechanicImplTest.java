package gameMechanics;

import frontend.FrontendImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import messageSystem.MessageSystemImpl;

import org.junit.*;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import resourceSystem.DatabaseResource;
import resourceSystem.GameSessionResource;
import databaseService.DatabaseServiceImpl;
import resourceSystem.ResourceFactory;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class GameMechanicImplTest {
    private static final String GAME_RES = "GameResource.xml";
    private static final String DB_RES = "DatabaseResource.xml";

    @Test
    public void testProcessMessages() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DOMException, NoSuchFieldException, SecurityException, IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
        ResourceFactory factory = ResourceFactory.getInstance();
        final MessageSystemImpl ms = new MessageSystemImpl();
        GameMechanicImpl gameMechanic = new GameMechanicImpl(ms, (GameSessionResource) factory.get(GAME_RES));

        gameMechanic.getSessions().put(1, new GameSession(1));
        gameMechanic.processMessages(1, 1000);
        Assert.assertTrue(gameMechanic.getSessions().get(1).getCountClicks() == 1000);
    }

    @Test
    public void testContainsMS() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DOMException, NoSuchFieldException, SecurityException, IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
        ResourceFactory factory = ResourceFactory.getInstance();
        final MessageSystemImpl ms = new MessageSystemImpl();
        GameMechanicImpl gameMechanic = new GameMechanicImpl(ms, (GameSessionResource) factory.get(GAME_RES));
        assertNotNull(gameMechanic.getMessageSystem());
    }

    @Test
    public void testGameMechanicSessions() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DOMException, NoSuchFieldException, SecurityException, IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
        ResourceFactory factory = ResourceFactory.getInstance();
        MessageSystemImpl ms = new MessageSystemImpl();
        GameMechanicImpl gameMechanic = new GameMechanicImpl(ms, (GameSessionResource) factory.get(GAME_RES));
        List<Integer> userIds = prepareList();
        gameMechanic.initSessions(userIds);

        Map<Integer, GameSession> sessions = gameMechanic.getSessions();
        assertTrue(sessions.containsKey(1) && sessions.containsKey(2));
    }

    private List<Integer> prepareList(){
        List<Integer> userIds = new ArrayList();
        userIds.add(1);
        userIds.add(2);
        return userIds;
    }
}
