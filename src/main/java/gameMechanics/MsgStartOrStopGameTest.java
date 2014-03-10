package gameMechanics;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import base.Msg;

import static org.mockito.Mockito.*;
import org.junit.Test;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import frontend.FrontendImpl;
import base.Address;


public class MsgStartOrStopGameTest {

    @Test
    public void testMsgStartOrStopGame() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DOMException, NoSuchFieldException, SecurityException, IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
        FrontendImpl frontendImpl = mock(FrontendImpl.class);
        GameMechanicImpl gameMechImpl = mock(GameMechanicImpl.class);
        Address from = mock(Address.class);
        Address to = mock(Address.class);
        when(frontendImpl.getAddress()).thenReturn(from);
        when(gameMechImpl.getAddress()).thenReturn(to);

        Msg msg = new MsgStartOrStopGame(frontendImpl.getAddress(), gameMechImpl.getAddress(), true);
        assertNotNull(msg.getTo());
        assertNotNull(msg.getFrom());
    }

}
