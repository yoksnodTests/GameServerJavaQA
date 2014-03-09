package gameMechanics;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import messageSystem.MessageSystemImpl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import frontend.FrontendImpl;
import resourceSystem.GameSessionResource;
import base.Address;
import base.MessageSystem;
import resourceSystem.ResourceFactory;

public class MsgStartOrStopGameTest {

    @Test
    public void testMsgStartOrStopGame() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DOMException, NoSuchFieldException, SecurityException, IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
        ResourceFactory factory = ResourceFactory.getInstance();
        MessageSystem ms = new MessageSystemImpl();
        FrontendImpl frontendImpl = new FrontendImpl(ms, (GameSessionResource) factory.get("GameResource.xml"));
        ms.addService(frontendImpl);
        MsgStartOrStopGame msg = new MsgStartOrStopGame(new Address(), new Address(), true);
        msg.exec(frontendImpl);
        assertNotNull(msg.getTo());
    }

}
