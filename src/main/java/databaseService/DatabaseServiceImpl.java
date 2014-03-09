package databaseService;

import java.util.Calendar;

import base.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import org.hibernate.service.ServiceRegistryBuilder;


import frontend.FrontendImpl;
import frontend.MsgUpdateUserId;

import resourceSystem.DatabaseResource;
import utils.TimeHelper;

public class DatabaseServiceImpl implements AccountService, Runnable {

    private static String TABLE_COLUMN = "userName";

    private Configuration configuration;
    private SessionFactory sessionFactory;
    private ServiceRegistryBuilder builder;
    private MessageSystem ms;
    private Address address;

    public DatabaseServiceImpl(MessageSystem ms, DatabaseResource resource) {
        this.ms = ms;
        this.address = new Address();
        ms.addService(this);
        prepareConfig();
        builder = new ServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        configuration.addAnnotatedClass(Gamer.class);
        sessionFactory = configuration.buildSessionFactory(builder.buildServiceRegistry());
    }

    private void prepareConfig() {
        configuration = new Configuration();
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/game");
        configuration.setProperty("hibernate.connection.username", "root");
        configuration.setProperty("hibernate.connection.password", "1234567");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
    }

    public Address getAddress() {
        return address;
    }

    public MessageSystem getMessageSystem() {
        return ms;
    }


    public Gamer initGamer(String userName, Integer sessionId) {
        Address address = getAddress();
        address.setThreadUsed(true);
        Session session = sessionFactory.openSession();
        Criteria where = session.createCriteria(Gamer.class).add(Restrictions.eq(TABLE_COLUMN, userName));
        Gamer testGamer = (Gamer) where.uniqueResult();
        testGamer = sendMsg(sessionId, testGamer, userName, session);
        session.close();
        address.setThreadUsed(false);
        return testGamer;
    }

    private Gamer sendMsg(int sessionId, Gamer testGamer, String userName, Session session) {
        int id = 0;
        Calendar calendar = null;
        int clicksTopResult = 0;
        if (testGamer == null) {
            Gamer newGamer = new Gamer();
            newGamer.setUserName(userName);
            onSave(session, newGamer);
            Criteria where = session.createCriteria(Gamer.class).add(Restrictions.eq(TABLE_COLUMN, userName));
            testGamer = (Gamer)where.uniqueResult();
            id = testGamer.getUserId();
            System.out.println(testGamer.getUserName() + "asdasdsadsadasdas");
        } else {
            clicksTopResult = testGamer.getBestCount();
            calendar = testGamer.getLastDate();
            testGamer.setLastDate(Calendar.getInstance());
            Transaction transaction = session.beginTransaction();
            session.update(testGamer);
            transaction.commit();
            id = testGamer.getUserId();
        }

        Address to = ms.getAddressService().getAddress(FrontendImpl.class);
        Msg newMsg = new MsgUpdateUserId(address, to, id, sessionId, clicksTopResult, calendar);
        ms.sendMessage(newMsg);
        return testGamer;
    }

    public void onSave(Session session, Gamer gamer) {
        Transaction transaction = session.beginTransaction();
        session.save(gamer);
        transaction.commit();
    }

    public void run() {
        while (true) {
            ms.execForAbonent(this);
            TimeHelper.sleep(100);
        }
    }

    public void saveResult(int id, int countClicks) {
        Session session = sessionFactory.openSession();
        Gamer gamer = (Gamer) session.load(Gamer.class, id);
        if (gamer.getBestCount() < countClicks) {
            gamer.setBestCount(countClicks);

            Transaction transaction = session.beginTransaction();
            session.update(gamer);
            transaction.commit();

        }
        session.close();

    }
}
