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

import resourceSystem.DatabaseResource;
import accountService.AccountServiceImpl;
import base.Address;
import base.MessageSystem;
import resourceSystem.ResourceFactory;

public class MsgSaveResultTest {

	@Test
	public void testMsgSaveResult() throws InstantiationException, IllegalAccessException, ClassNotFoundException, DOMException, NoSuchFieldException, SecurityException, IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
        ResourceFactory factory = ResourceFactory.getInstance();
		MessageSystem ms = new MessageSystemImpl();
		AccountServiceImpl accountServiceImpl = new AccountServiceImpl(ms, (DatabaseResource)factory.get("DatabaseResource.xml"));
		ms.addService(accountServiceImpl);
		MsgSaveResult msgSaveResult = new MsgSaveResult(new Address(), new Address(), 1, 100500);
		msgSaveResult.exec(accountServiceImpl);		
		assertNotNull(msgSaveResult);
	}

	@Test
	public void testGetTo() {
		MsgSaveResult msgSaveResult = new MsgSaveResult(new Address(), new Address(), 1, 100500);
		assertNotNull(msgSaveResult.getTo());
	}
}
