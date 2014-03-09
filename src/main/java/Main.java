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

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Main {

    private static final int PORT = 8083;
    private static final String GAME_RESOURCE_FILE_NAME = "GameResource.xml";
    private static final String DB_RESOURCE_FILE_NAME = "DatabaseResource.xml";
    private static final int SERVICES_POOL_SIZE = 3;

    public static void main(String[] args) throws Exception {


        ResourceFactory factory = ResourceFactory.getInstance();
        MessageSystem ms = new MessageSystemImpl();
        Frontend frontend = new FrontendImpl(ms, (GameSessionResource) factory.get(GAME_RESOURCE_FILE_NAME));
        GameMechanic gameMechanic = new GameMechanicImpl(ms, (GameSessionResource) factory.get("GameResource.xml"));
        Server server = new Server(PORT);

        Queue<Runnable> dbServices = new LinkedList<>();
        DatabaseResource res = (DatabaseResource) factory.get(DB_RESOURCE_FILE_NAME);
        dbServices.add(new DatabaseServiceImpl(ms, res));
        dbServices.add(new DatabaseServiceImpl(ms, res));
        dbServices.add(new DatabaseServiceImpl(ms, res));

        Executors.newSingleThreadExecutor().execute((FrontendImpl) frontend);
        for (Runnable each : dbServices){
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(each);
        }
        Executors.newSingleThreadExecutor().execute((GameMechanicImpl) gameMechanic);
        server.setHandler((FrontendImpl) frontend);

        server.start();
        server.join();
    }

}
