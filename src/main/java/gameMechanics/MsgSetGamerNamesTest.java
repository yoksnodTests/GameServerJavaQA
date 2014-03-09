package gameMechanics;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import messageSystem.MessageSystemImpl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import resourceSystem.GameSessionResource;
import frontend.UserSession;
import base.Address;
import base.MessageSystem;
import resourceSystem.ResourceFactory;

public class MsgSetGamerNamesTest {

    @Test
    public void testMsgSetGamerNames() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DOMException, NoSuchFieldException, SecurityException, IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
        ResourceFactory factory = ResourceFactory.getInstance();
        MessageSystem ms = new MessageSystemImpl();
        GameMechanicImpl gameMechanic = new GameMechanicImpl(ms, (GameSessionResource) factory.get("GameResource.xml"));
        ms.addService(gameMechanic);
        assertNotNull(gameMechanic.getAddress());
    }

}
