package com.yadev.dao;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.yadev.dto.CompanyDto;
import com.yadev.dto.PaymentFilter;
import com.yadev.entity.Payment;
import com.yadev.entity.User;
import com.yadev.util.HibernateTestUtil;
import com.yadev.util.TestDataImporter;
import lombok.Cleanup;
import org.assertj.core.data.Percentage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTest {

    private final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
    private final UserDao userDao = UserDao.getInstance();

    @BeforeAll //because only select queries
    public void initDb() {
        TestDataImporter.importData(sessionFactory);
    }

    @AfterAll
    public void finish() {
        sessionFactory.close();
    }

    @Test
    void findAll() {
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var results = userDao.findAll(session);
        assertThat(results).hasSize(5);

        List<String> fullNames = results.stream().map(User::fullName).collect(toList());
        assertThat(fullNames).containsExactlyInAnyOrder("Bill Gates", "Steve Jobs", "Monica Harmonica", "Tim Cook", "Jeff Bezos");

        session.getTransaction().commit();
    }

    @Test
    void findAllByFirstName() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<User> results = userDao.findAllByFirstName(session, "Bill");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).fullName()).isEqualTo("Bill Gates");

        session.getTransaction().commit();
    }

    @Test
    void findLimitedUsersOrderedByBirthday() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        int limit = 3;
        List<User> results = userDao.findUsersOrderedByBirthdayWithLimit(session, limit);
        assertThat(results).hasSize(limit);
        System.out.println(results);

        List<String> fullNames = results.stream().map(User::fullName).collect(toList());
        assertThat(fullNames).contains("Tim Cook", "Steve Jobs", "Bill Gates");

        session.getTransaction().commit();
    }

    @Test
    void findAllByCompanyName() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        var result = userDao.findAllByCompanyName(session, "Apple");
        assertThat(result).hasSize(3);
        System.out.println(result);

        var fullNames = result.stream()
                .map(User::fullName)
                .collect(toList());
        assertThat(fullNames).contains("Monica Harmonica", "Tim Cook", "Steve Jobs");

        session.getTransaction().commit();
    }

    @Test
    void findAllPaymentsByCompanyName() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        var result = userDao.findAllPaymentsByCompanyName(session, "Apple");
        assertThat(result).hasSize(9);

        var amounts = result.stream().map(Payment::getAmount).collect(toList());
        assertThat(amounts).contains(5, 7, 10, 250, 350, 450, 150, 200, 230);

        session.getTransaction().commit();
    }

    @Test
    void findAveragePaymentAmountByFirstAndLastNames() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        var filter = PaymentFilter.builder()
                .firstName("Jeff")
                .lastName("Bezos")
                .build();
        var result = userDao.findAveragePaymentAmountByFirstAndLastNames(session, filter);
        assertThat(result).isCloseTo(416.0, Percentage.withPercentage(5.0));

        session.getTransaction().commit();
    }

    @Test
    void findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Tuple> results = userDao.findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(session);
        assertThat(results).hasSize(3);

        List<String> orgNames = results.stream()
                .map(it -> it.get(0, String.class))
                .collect(toList());
        assertThat(orgNames).contains("Apple", "Amazon", "Microsoft");

        List<Double> orgAvgPayments = results.stream()
                .map(it -> it.get(1, Double.class))
                .collect(toList());
        assertThat(orgAvgPayments).contains(416.6666666666667, 183.55555555555554, 200.0);

        session.getTransaction().commit();
    }

    @Test
    void isItPossible() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<Tuple> results = userDao.isItPossible(session);
        assertThat(results).hasSize(2);

        List<String> names = results.stream().map(r -> r.get(0, User.class).fullName()).collect(toList());
        assertThat(names).contains("Jeff Bezos", "Steve Jobs");

        List<Double> averagePayments = results.stream().map(r -> r.get(1, Double.class)).collect(toList());
        assertThat(averagePayments).contains(416.6666666666667, 350.0);

        session.getTransaction().commit();
    }

}
