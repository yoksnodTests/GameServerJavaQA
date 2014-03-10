package gameMechanics;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import base.Address;
import junit.framework.Assert;
import messageSystem.MessageSystemImpl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import resourceSystem.GameSessionResource;
import frontend.FrontendImpl;
import base.MessageSystem;
import resourceSystem.ResourceFactory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MsgVictoryTest {

    private static final String YOU_LOSE = "You lose";
    private static final String YOU_WIN = "You win";

	@Test
	public void testMsgVictory() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DOMException, NoSuchFieldException, SecurityException, IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
        FrontendImpl frontendImpl = mock(FrontendImpl.class);
        GameMechanicImpl gameMechImpl = mock(GameMechanicImpl.class);
        Address from = mock(Address.class);
        Address to = mock(Address.class);
        when(frontendImpl.getAddress()).thenReturn(from);
        when(gameMechImpl.getAddress()).thenReturn(to);
        MsgVictory msgVictory = new MsgVictory(gameMechImpl.getAddress(),
                frontendImpl.getAddress(),
                1, 20, YOU_LOSE );
		msgVictory.exec(frontendImpl);
        Assert.assertNotNull(msgVictory.getTo());
        Assert.assertNotNull(msgVictory.getFrom());
    }


}
