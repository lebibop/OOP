package oop.Helpers;

import oop.Model.Client;
import oop.Model.Report;
import oop.Model.Room;
import oop.Model.Worker;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import java.util.Properties;

/**
 * Класс HibernateUtil предоставляет методы для работы с Hibernate.
 * Он содержит методы для создания объекта SessionFactory и закрытия его.
 * @author lebibop
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    /**
     * Возвращает объект SessionFactory, который используется для создания сессий Hibernate.
     * Если объект SessionFactory еще не создан, то создает его с помощью настроек, указанных в методе.
     * @return объект SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties settings = new Properties();
                settings.put(Environment.URL, "jdbc:mysql://localhost:3306/mydb");
                settings.put(Environment.USER, "root");
                settings.put(Environment.PASS, "1234");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
                settings.put(Environment.SHOW_SQL, "false");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, "update");

                configuration.setProperties(settings);
                configuration.addAnnotatedClass(Worker.class);
                configuration.addAnnotatedClass(Client.class);
                configuration.addAnnotatedClass(Room.class);
                configuration.addAnnotatedClass(Report.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    /**
     * Закрывает объект SessionFactory и освобождает все связанные с ним ресурсы.
     */
    public static void shutdown() {
        getSessionFactory().close();
    }
}
