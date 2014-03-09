package gameMechanics;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

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

public class MsgVictoryTest {

	@Test
	public void testMsgVictory() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DOMException, NoSuchFieldException, SecurityException, IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
        ResourceFactory factory = ResourceFactory.getInstance();
		MessageSystem ms = new MessageSystemImpl();
		GameMechanic gameMechanic = new GameMechanicImpl(ms, (GameSessionResource)factory.get("GameResource.xml"));
		ms.addService(gameMechanic);
		FrontendImpl frontendImpl = new FrontendImpl(ms, (GameSessionResource)factory.get("GameResource.xml"));
		ms.addService(frontendImpl);

		MsgVictory msgVictory = new MsgVictory(gameMechanic.getAddress(), ms.getAddressService().getAddress(FrontendImpl.class), 1, 20, "�� �����������" + " ���� ��������" );
		msgVictory.exec(frontendImpl);
        Assert.assertNotNull(msgVictory.getTo());
    }
}
