package com.yadev;

import com.yadev.entity.*;
import com.yadev.util.HibernateTestUtil;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.QueryHints;
import org.junit.jupiter.api.Test;
import javax.persistence.FlushModeType;
import java.sql.SQLException;

class HibernateRunnerTest {
    @Test
    void checkHQL(){
        try(var sessionFactory = HibernateTestUtil.buildSessionFactory();
        var session = sessionFactory.openSession();){
            session.beginTransaction();

            var list = session.createNamedQuery(
                    "findUserByName"
//                    "select u from User u where u.personalInfo.firstname = ?1", User.class)
                    , User.class)
//                    .setParameter(1, "Ivan")
                    .setParameter("firstname", "Ivan")
                    .setParameter("companyName", "Google")
                    .setFlushMode(FlushModeType.AUTO)
                    .setHint(QueryHints.FETCH_SIZE, "50")
                    .list();

            var countRows = session.createQuery("update User u set u.role = 'ADMIN'")
                    .executeUpdate();

            var resultClass = session.createNativeQuery("PLAIN OLD SQL", User.class);


            session.getTransaction().commit();
        }
    }

    @Test
    void localeInfo() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var company = session.get(Company.class, 43);

            company.getEmployees().forEach((k, v) -> System.out.println(v));
            session.getTransaction().commit();
        }
    }

    @Test
    void checkManyToMany() {
        try (SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            var user = session.get(User.class, 10L);
            var profile = session.get(Profile.class, 1L);
            profile.setUser(user);
            var chat = session.get(Chat.class, 1L);

            var userChat = UserChat.builder()
                    .build();
            userChat.setUser(user);
            userChat.setChat(chat);

            session.save(userChat);
            System.out.println();

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOneToOne() {
        Company company = null;
        try (
                SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
                Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = null;

            var profile = Profile.builder()
                    .street("Kolosova, 18")
                    .language("ru")
                    .build();
            profile.setUser(user);

            session.save(user);
//            session.save(profile);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOrphanDeletion() {
        Company company = null;
        try (
                SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
                Session session = sessionFactory.openSession()) {
            session.beginTransaction();

//            company = session.get(Company.class, 31);
//            company.getEmployees().removeIf(user -> user.getUsername().equals("user3"));
//
//            for (User employee : company.getEmployees()) {
//                System.out.println(employee);
//            }

            session.getTransaction().commit();
        }
    }

    @Test
    void checkLazyInitialization() {
        Company company = null;
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            company = session.get(Company.class, 31);
            System.out.println(company.getEmployees());
            session.getTransaction().commit();
        }
//        for (User user : company.getEmployees()) {
//            System.out.println(user);
//        }
    }

    @Test
    void deleteUser() {

        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();

        session.beginTransaction();
        var user = session.get(User.class, 7L);
        session.delete(user);
        session.getTransaction().commit();
    }

    @Test
    void checkIfCompanyIsSavedWhenCascadeTypeALL() { //answer is YES

        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            var company = Company.builder()
                    .name("Umbrella Corp")
                    .build();
//            session.save(company);

            User user = null;

            var profile = Profile.builder()
                    .language("en")
                    .street("Elm street, 5")
                    .build();
            profile.setUser(user);

            session.save(profile);

            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
    }


    @Test
    void deleteCompany() {
        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();

        session.beginTransaction();

        var company = session.get(Company.class, 45);
        session.delete(company);

        session.getTransaction().commit();
    }

    @Test
    void addUserToNewCompany() {
        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();

        session.beginTransaction();

        var company = session.get(Company.class, 43);

//        var user1 = User.builder()
//                .username("Tooran")
//                .personalInfo(
//                        PersonalInfo.builder()
//                                .firstname("Dayana")
//                                .lastname("Control")
//                                .birthDate(new Birthday(LocalDate.of(2000, 4, 1)))
//                                .build()
//                )
//                .role(Role.ADMIN)
////                .info("""
////                        {
////                        "key1":"value1"
////                        }""")
//                .build();
        User user1 = null;
//
//        var user2 = User.builder()
//                .username("Shmogol")
//                .personalInfo(
//                        PersonalInfo.builder()
//                                .firstname("Elik")
//                                .lastname("Gogol")
//                                .birthDate(new Birthday(LocalDate.of(2001, 5, 2)))
//                                .build()
//                )
//                .role(Role.USER)
////                .info("""
////                        {
////                        "key2":"value2"
////                        }""")
//                .build();
        User user2 = null;

//        var user3 = User.builder()
//                .username("Susanin")
//                .personalInfo(PersonalInfo.builder()
//                        .firstname("Ivan")
//                        .lastname("Susanin")
//                        .build())
//                .build();
        User user3 = null;

//        var company = Company.builder()
//                .name("Amazon")
//                .build();

//        company.addUser(user1);
//        company.addUser(user2);
//        company.addUser(user3);

//        session.save(company);
        session.save(user1);
        session.save(user2);
        session.save(user3);

        session.getTransaction().commit();

        System.out.println("Company:");
//        for (User user : company.getEmployees()) {
//            System.out.println(user);
//        }
    }

    @Test
    void oneToMany() {
        @Cleanup var sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var company = session.get(Company.class, 21);
        System.out.println(company);

        session.getTransaction().commit();
    }

    @Test
    void checkReflectionAPI() throws SQLException {
//        var user = User.builder()
//                .username("sauron")
//                .firstname("aleksey")
//                .lastname("y")
//                .birthDate(new Birthday(LocalDate.of(1988, 1, 13)))
//                .build();
//
//        String tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
//                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
//                .orElse(user.getClass().getName());
//
//        //Checking if annotation is present
////        For this purpose we use Optional.ofNullable().orElse(Field:Name)...
//        var declaredFields = user.getClass().getDeclaredFields();
//        var columnNames = Arrays.stream(declaredFields)
//                .map(field ->
//                        Optional.ofNullable(field.getAnnotation(Column.class))
//                                .map(Column::name)
//                                .orElse(field.getName()))
//                .collect(Collectors.joining(", "));
//
//        var columnValues = Arrays.stream(declaredFields).map(field -> "?").collect(Collectors.joining(", "));
//
//        String INSERT_SQL = """
//                insert
//                into
//                %s
//                (%s)
//                values
//                (%s)
//                """;
//
//
//        Connection connection = null;
//        connection.prepareStatement(INSERT_SQL.formatted(tableName, columnNames, columnValues));
    }

}