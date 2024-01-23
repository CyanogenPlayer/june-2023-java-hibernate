import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();
        Metadata metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(Word.class)
                .addAnnotatedClass(Car.class)
                .getMetadataBuilder()
                .build();

        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.persist(new Word("lol"));
        session.persist(new Word("kek"));
        session.persist(new Word("hello"));
        System.out.println("created and added words");

        session.persist(new Car("toyoda", CarType.FAMILY, 100, 2000, 2002));
        session.persist(new Car("bmw", CarType.SUPER_CAR, 1000, 20000, 2022));
        session.persist(new Car("mercedes", CarType.SPORT_CAR, 120, 200_000, 2012));
        session.getTransaction().commit();

        List<String> values = session.createNativeQuery("select value from word").list();
        System.out.println(values);

        List<Car> cars = session.createNativeQuery("select * from car", Car.class).list();
        System.out.println(cars);

        session.close();
        sessionFactory.close();
    }
}
