package com.yadev.util;

import com.yadev.entity.*;
import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.time.Month;

@UtilityClass
public class TestDataImporter {
    public void importData(SessionFactory sessionFactory) {
        @Cleanup Session session = sessionFactory.openSession();

        Company microsoft = saveCompany(session, "Microsoft");
        Company apple = saveCompany(session, "Apple");
        Company amazon = saveCompany(session, "Amazon");

        User billGates = saveUser(session,
                "Bill",
                "Gates",
                LocalDate.of(1955, Month.OCTOBER, 28),
                microsoft);
        User steveJobs = saveUser(session,
                "Steve",
                "Jobs",
                LocalDate.of(1955, Month.FEBRUARY, 24),
                apple);
        User monicaHarmonica = saveUser(session,
                "Monica",
                "Harmonica",
                LocalDate.of(1982, Month.MARCH, 1),
                apple);
        User timCook = saveUser(session,
                "Tim",
                "Cook",
                LocalDate.of(1960, Month.NOVEMBER, 1),
                apple);
        User jeffBezos = saveUser(session,
                "Jeff",
                "Bezos",
                LocalDate.of(1964, Month.JANUARY, 12),
                amazon);

        savePayment(session, billGates, 100);
        savePayment(session, billGates, 200);
        savePayment(session, billGates, 300);

        savePayment(session, steveJobs, 250);
        savePayment(session, steveJobs, 350);
        savePayment(session, steveJobs, 450);

        savePayment(session, monicaHarmonica, 5);
        savePayment(session, monicaHarmonica, 10);
        savePayment(session, monicaHarmonica, 7);

        savePayment(session, timCook, 150);
        savePayment(session, timCook, 200);
        savePayment(session, timCook, 230);

        savePayment(session, jeffBezos, 500);
        savePayment(session, jeffBezos, 500);
        savePayment(session, jeffBezos, 250);
    }

    private void savePayment(Session session, User user, Integer amount) {
        Payment payment = Payment.builder()
                .amount(amount)
                .receiver(user)
                .build();
        session.save(payment);
    }

    private User saveUser(Session session,
                          String firstname,
                          String lastname,
                          LocalDate birthday,
                          Company company) {
        User user = User.builder()
                .username(firstname + lastname)
                .personalInfo(PersonalInfo.builder()
                        .firstname(firstname)
                        .lastname(lastname)
                        .birthDate(birthday)
                        .build())
                .company(company)
                .build();
        session.save(user);

        return user;
    }

    private Company saveCompany(Session session, String companyName) {
        Company company = Company.builder()
                .name(companyName)
                .build();
        session.save(company);

        return company;
    }
}
