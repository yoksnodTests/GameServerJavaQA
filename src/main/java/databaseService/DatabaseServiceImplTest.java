package databaseService;

import frontend.FrontendImpl;
import gameMechanics.GameMechanic;
import gameMechanics.GameMechanicImpl;

import java.io.IOException;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import messageSystem.MessageSystemImpl;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import base.MessageSystem;
import resourceSystem.DatabaseResource;
import resourceSystem.GameSessionResource;
import resourceSystem.ResourceFactory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DatabaseServiceImplTest {

    private static final String GAME_RES = "GameResource.xml";
    private static final String DB_RES = "DatabaseResource.xml";

    @Test
    public void testGamerProfileNotExist() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DOMException, NoSuchFieldException, SecurityException, IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
        ResourceFactory factory = ResourceFactory.getInstance();
        MessageSystem ms = new MessageSystemImpl();
        GameMechanic gameMechanic = new GameMechanicImpl(ms,
                (GameSessionResource) factory.get("GameResource.xml"));
        ms.addService(gameMechanic);
        FrontendImpl frontendImpl = new FrontendImpl(ms,
                (GameSessionResource) factory.get("GameResource.xml"));
        ms.addService(frontendImpl);

        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String userName = sb.toString();
        DatabaseServiceImpl databaseServiceImpl = new DatabaseServiceImpl(ms, (DatabaseResource) factory.get(DB_RES));
        Gamer gamer = databaseServiceImpl.initGamer(userName, 282);
        Assert.assertNotNull(gamer.getUserName());
    }

    @Test
    public void testGamerName() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DOMException, NoSuchFieldException, SecurityException, IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
        ResourceFactory factory = ResourceFactory.getInstance();
        MessageSystem ms = new MessageSystemImpl();
        GameMechanic gameMechanic = new GameMechanicImpl(ms,
                (GameSessionResource) factory.get(GAME_RES));
        ms.addService(gameMechanic);
        FrontendImpl frontendImpl = new FrontendImpl(ms,
                (GameSessionResource) factory.get(DB_RES));
        ms.addService(frontendImpl);
        DatabaseServiceImpl databaseServiceImpl = new DatabaseServiceImpl(ms, (DatabaseResource) factory.get(DB_RES));
        Gamer gamer = new Gamer();
        gamer.setUserName("donskoy");
        Gamer mockGamer = mock(Gamer.class);
        when(mockGamer.getUserName()).thenReturn("donskoy");
        Gamer realGamer = databaseServiceImpl.initGamer(gamer.getUserName(), 282);
        Assert.assertEquals(mockGamer.getUserName(), realGamer.getUserName());
    }

    @Test
    public void testGamerTopResult() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DOMException, NoSuchFieldException, SecurityException, IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
        ResourceFactory factory = ResourceFactory.getInstance();
        MessageSystem ms = new MessageSystemImpl();
        GameMechanic gameMechanic = new GameMechanicImpl(ms,
                (GameSessionResource) factory.get(GAME_RES));
        ms.addService(gameMechanic);
        FrontendImpl frontendImpl = new FrontendImpl(ms,
                (GameSessionResource) factory.get(DB_RES));
        ms.addService(frontendImpl);
        DatabaseServiceImpl databaseServiceImpl = new DatabaseServiceImpl(ms, (DatabaseResource) factory.get(DB_RES));
        Gamer mockGamer = mock(Gamer.class);
        when(mockGamer.getBestCount()).thenReturn(-1);
        Gamer realGamer = databaseServiceImpl.initGamer("donskoy", 100500);
        Assert.assertNotSame(mockGamer.getBestCount(), realGamer.getBestCount());
    }
}
