import messageSystem.MessageSystemImpl;

import org.eclipse.jetty.server.Server;


import databaseService.DatabaseServiceImpl;


import resourceSystem.DatabaseResource;
import resourceSystem.GameSessionResource;
import resourceSystem.ResourceFactory;


import base.Frontend;
import base.MessageSystem;

import frontend.FrontendImpl;
import gameMechanics.GameMechanic;
import gameMechanics.GameMechanicImpl;


public class Main {

    private static final int PORT = 8083;
    private static final String GAME_RESOURCE_FILE_NAME = "GameResource.xml";
    private static final String DB_RESOURCE_FILE_NAME = "DatabaseResource.xml";

    public static void main(String[] args) throws Exception {


        ResourceFactory factory = ResourceFactory.getInstance();
        MessageSystem ms = new MessageSystemImpl();
        Frontend frontend = new FrontendImpl(ms, (GameSessionResource) factory.get(GAME_RESOURCE_FILE_NAME));
        GameMechanic gameMechanic = new GameMechanicImpl(ms, (GameSessionResource) factory.get("GameResource.xml"));
        Server server = new Server(PORT);


        new Thread((FrontendImpl) frontend).start();

        new Thread(new DatabaseServiceImpl(ms, (DatabaseResource) factory.get(DB_RESOURCE_FILE_NAME))).start();
        new Thread(new DatabaseServiceImpl(ms, (DatabaseResource) factory.get(DB_RESOURCE_FILE_NAME))).start();
        new Thread(new DatabaseServiceImpl(ms, (DatabaseResource) factory.get(DB_RESOURCE_FILE_NAME))).start();
        new Thread((GameMechanicImpl) gameMechanic).start();
        server.setHandler((FrontendImpl) frontend);

        server.start();
        server.join();


    }

}
