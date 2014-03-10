package gameMechanics;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import base.Frontend;
import base.Msg;
import frontend.FrontendImpl;
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
        GameMechanicImpl gameMechImpl = mock(GameMechanicImpl.class);
        Address to = mock(Address.class);
        FrontendImpl frontendImpl = mock(FrontendImpl.class);
        Address from = mock(Address.class);
        when(gameMechImpl.getAddress()).thenReturn(to);
        when(frontendImpl.getAddress()).thenReturn(from);
        Collection<UserSession> sessions = new ArrayList<>();
        sessions.add(new UserSession(100500));
        sessions.add(new UserSession(100501));
        Msg msg = new MsgSetGamerNames(frontendImpl.getAddress(),
                                       gameMechImpl.getAddress(),
                                       sessions);
        assertNotNull(msg.getFrom());
        assertNotNull(msg.getTo());
    }

}
