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
                .addAnnotatedClass(Car.class)
                .addAnnotatedClass(DriveLicense.class)
                .addAnnotatedClass(Owner.class)
                .getMetadataBuilder()
                .build();

        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Car car1 = new Car("toyoda", CarType.FAMILY, 100, 2000, 2002);
        Car car2 = new Car("bmw", CarType.SUPER_CAR, 1000, 20000, 2022);
        DriveLicense license = new DriveLicense("blablabla");

        session.persist(car1);
        session.persist(car2);
        session.persist(license);

        Owner owner = new Owner("vova", List.of(car1, car2), license);

        session.persist(owner);

        session.getTransaction().commit();

        List<Owner> owners = session.createQuery("select o from Owner o", Owner.class).list();
        System.out.println(owners);

        session.close();
        sessionFactory.close();
    }
}
