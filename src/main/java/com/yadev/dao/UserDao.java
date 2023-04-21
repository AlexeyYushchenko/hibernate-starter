package com.yadev.dao;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.yadev.dto.PaymentFilter;
import com.yadev.entity.Payment;
import com.yadev.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import java.util.List;
import static com.yadev.entity.QCompany.company;
import static com.yadev.entity.QPayment.payment;
import static com.yadev.entity.QUser.user;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {
    private static class UserDaoHolder {
        private static final UserDao INSTANCE = new UserDao();
    }

    public static UserDao getInstance() {
        return UserDaoHolder.INSTANCE;
    }

    /**
     * Returns all users
     */
    public List<User> findAll(Session session) {

        //querydsl

        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .fetch();

        //HQL

//        return session.createQuery("select u from User u", User.class)
//                .list();

        //CRITERIA API

//        var cb = session.getCriteriaBuilder();
//        var criteria = cb.createQuery(User.class);
//        var user = criteria.from(User.class);

//        criteria.select(user);
//
//        return session.createQuery(criteria).list();
    }

    /**
     * @param session
     * @param firstname
     * @return all users with required firstname
     */
    public List<User> findAllByFirstName(Session session, String firstname) {

        //querydsl
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .where(user.personalInfo.firstname.eq(firstname))
                .fetch();

//        HQL

//        return session.createQuery("select u from User u " +
//                        "where u.personalInfo.firstname = :firstname", User.class)
//                .setParameter("firstname", firstname)
//                .list();

//        CRITERIA API

//        var cb = session.getCriteriaBuilder();
//        var criteria = cb.createQuery(User.class);
//        var user = criteria.from(User.class);
//
//        criteria.select(user).where(
//                cb.equal(user.get(User_.PERSONAL_INFO).get(PersonalInfo_.FIRSTNAME), firstname)
//        );
//
//        return session.createQuery(criteria)
//                .list();
    }

    /**
     * @param session
     * @param limit
     * @return {limit} amount of users ordered by the birthday.
     */
    public List<User> findUsersOrderedByBirthdayWithLimit(Session session, int limit) {

        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .orderBy(user.personalInfo.birthDate.asc())
                .limit(limit)
                .fetch();

//        HQL

//        return session.createQuery("select u from User u " +
//                        "order by u.personalInfo.birthDate", User.class)
//                .setMaxResults(limit)
////                .setMaxResults(offset)
//                .list();

//        CRITERIA API

//        var cb = session.getCriteriaBuilder();
//        var criteria = cb.createQuery(User.class);
//        var user = criteria.from(User.class);
//
//        criteria.select(user).orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.birthDate)));
//
//        return session.createQuery(criteria)
//                .setMaxResults(limit)
//                .list();
    }

    /**
     * @param session
     * @param companyName
     * @return all users from a company with the {companyName}
     */
    public List<User> findAllByCompanyName(Session session, String companyName) {

        return new JPAQuery<User>(session)
                .select(user)
//                .from(user)
//                .join(user.company, company)

                .from(company)
                .join(company.employees, user)
                .where(company.name.eq(companyName))
                .fetch();

//        HQL

//        return session.createQuery("select u from Company c join c.employees u where c.name = :companyName", User.class)
//                .setParameter("companyName", companyName)
//                .list();

//        CRITERIA API

//        var cb = session.getCriteriaBuilder();
//        var criteria = cb.createQuery(User.class);
////        var user = criteria.from(User.class);
////
////        criteria.select(user)
////                .where(cb.equal(user.get(User_.company).get(Company_.NAME), companyName));
//
//        var company = criteria.from(Company.class);
//        var users = company.join(Company_.employees);
//
//        criteria.select(users)
//                .where(cb.equal(company.get(Company_.NAME), companyName));
//
//        return session.createQuery(criteria)
//                .list();
    }

    /**
     * Возвращает все выплаты, полученные сотрудниками компании с указанными именем,
     * упорядоченные по имени сотрудника, а затем по размеру выплаты
     */

    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {

        return new JPAQuery<Payment>(session)
                .select(payment)
                .from(payment)
                .join(payment.receiver, user)
                .join(user.company)
                .where(payment.receiver.company.name.eq(companyName))
                .fetch();

        //        HQL

//        return session.createQuery("select p from Payment p " +
//                        "join p.receiver u " +
//                        "join u.company c " +
//                        "where c.name = :companyName " +
//                        "order by u.personalInfo.firstname, p.amount", Payment.class)
//                .setParameter("companyName", companyName)
//                .list();

        //        CRITERIA API

//        var cb = session.getCriteriaBuilder();
//        var criteria = cb.createQuery(Payment.class);
//        var payment = criteria.from(Payment.class);
//        var user = payment.join(Payment_.receiver);
//        var company = user.join(User_.company);
//
//        criteria.select(payment)
//                .where(cb.equal(company.get(Company_.name), companyName))
//                .orderBy(
//                        cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)),
//                        cb.asc(payment.get(Payment_.amount))
//                );
//
//        return session.createQuery(criteria)
//                .list();
    }

    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, PaymentFilter filter) {
//        List<Predicate> predicates = new ArrayList<>();
//        if (filter.getFirstName() != null) {
//            predicates.add(user.personalInfo.firstname.eq(filter.getFirstName()));
//        }
//        if (filter.getLastName() != null) {
//            predicates.add(user.personalInfo.lastname.eq(filter.getLastName()));
//        }

        var predicate = QPredicate.builder()
                .add(filter.getFirstName(), user.personalInfo.firstname::eq)
                .add(filter.getFirstName(), user.personalInfo.firstname::eq)
                .buildAnd();

        return new JPAQuery<Double>(session)
                .select(payment.amount.avg())
                .from(payment)
                .join(payment.receiver, user)
                .where(predicate)
                .fetchOne();

        //HQL

//        return session.createQuery("select avg (p.amount) " +
//                                "from Payment p " +
//                                "join p.receiver u " +
//                                "where u.personalInfo.firstname = :firstName " +
//                                "and u.personalInfo.lastname = :lastName",
//                        Double.class)
//                .setParameter("firstName", firstName)
//                .setParameter("lastName", lastName)
//                .uniqueResult();

//        CRITERIA API

//        var cb = session.getCriteriaBuilder();
//        var criteria = cb.createQuery(Double.class);
//        var payment = criteria.from(Payment.class);
//        var user = payment.join(Payment_.receiver);
//
//        List<Predicate> predicates = new ArrayList<>();
//        if (firstName != null) {
//            predicates.add(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName));
//        }
//        if (lastName != null) {
//            predicates.add(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.lastname), lastName));
//        }
//
//        criteria.select(cb.avg(payment.get(Payment_.amount)))
//                .where(
//                        predicates.toArray(Predicate[]::new)
//                );
//
//        return session.createQuery(criteria)
//                .uniqueResult();
    }


    public List<Tuple> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {

        return new JPAQuery<Tuple>(session)
                .select(company.name, payment.amount.avg())
                .from(company)
                .join(company.employees, user)
                .join(user.payments, payment)
                .groupBy(company.name)
                .orderBy(company.name.asc())
                .fetch();

//        HQL

//        return session.createQuery("select c.name, avg(p.amount) " +
//                        "from Company c " +
//                        "join c.employees u " +
//                        "join u.payments p " +
//                        "group by c.name " +
//                        "order by c.name", Object[].class)
//                .list();

//        CRITERIA API

//        var cb = session.getCriteriaBuilder();
//        var criteria = cb.createQuery(CompanyDto.class);
//        var company = criteria.from(Company.class);
//        var user = company.join(Company_.employees);
//        var payment = user.join(User_.payments);

////        criteria.select(cb.array(company.get(Company_.name), cb.avg(payment.get(Payment_.amount))))
////                .groupBy(company.get(Company_.name))
////                .orderBy(cb.asc(company.get(Company_.name)));
//
////        criteria.multiselect(
////                        company.get(Company_.name),
////                        cb.avg(payment.get(Payment_.amount))
////                )
//        criteria.select(
//                        cb.construct
//                                (
//                                        CompanyDto.class,
//                                        cb.avg(payment.get(Payment_.amount))
//                                )
//                )
//                .groupBy(company.get(Company_.name))
//                .orderBy(cb.asc(company.get(Company_.name)));
//
//        return session.createQuery(criteria)
//                .list();
    }

    /**
     * возвращает список: сотрудник (объект), средний размер выплат, но только для тех сотрудников,
     * чей средний размер выплат больше среднего размера выплат всех сотрудников.
     * Упорядочить по имени сотрудника
     *
     * @param session
     * @return
     */

    public List<Tuple> isItPossible(Session session) {

        return new JPAQuery<Tuple>(session)
                .select(user, payment.amount.avg())
                .from(user)
                .join(user.payments, payment)
                .groupBy(user.id)
                .having(payment.amount.avg().gt(
                        new JPAQuery<Double>(session)
                                .select(payment.amount.avg())
                                .from(payment)
                )).orderBy(user.personalInfo.firstname.asc())
                .fetch();

//        HQL

//        return session.createQuery("select u, avg(p.amount) " +
//                        "from User u " +
//                        "join u.payments p " +
//                        "group by u " +
//                        "having avg(p.amount) > (select avg(p.amount) from Payment p) " +
//                        "order by u.personalInfo.firstname", Object[].class)
//                .list();

//        var cb = session.getCriteriaBuilder();
//        var criteria = cb.createQuery(Tuple.class);
//
//        var user = criteria.from(User.class);
//        var payment = user.join(User_.payments);
//
//        var subquery = criteria.subquery(Double.class);
//        var subqueryPayment = subquery.from(Payment.class);
//
//        criteria.select(
//                        cb.tuple(
//                                user,
//                                cb.avg(payment.get(Payment_.amount))
//                        )
//                ).groupBy(user)
//                .having(cb.greaterThan(
//                        cb.avg(payment.get(Payment_.amount)),
//                        subquery.select(cb.avg(subqueryPayment.get(Payment_.amount)))))
//                .orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)));
//
//
////        var subquery = criteria.subquery(cb.avg(payment.get(Payment_.amount)));
////
////        criteria.multiselect(user, cb.avg(payment.get(Payment_.amount)))
////                .groupBy(user)
////                .having(cb.equal(cb.avg(payment.get(Payment_.amount)), subquery))
////                .orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)));
//
//        return session.createQuery(criteria)
//                .list();
    }

}
