package com.yadev;

import com.yadev.dao.CompanyRepository;
import com.yadev.dao.UserRepository;
import com.yadev.dto.UserCreateDto;
import com.yadev.entity.PersonalInfo;
import com.yadev.entity.User;
import com.yadev.interceptor.TransactionInterceptor;
import com.yadev.mapper.CompanyReadMapper;
import com.yadev.mapper.UserCreateMapper;
import com.yadev.mapper.UserReadMapper;
import com.yadev.service.UserService;
import com.yadev.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

@Slf4j
public class HibernateRunner {

    //    private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);
    @Transactional
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        try (var sessionFactory = HibernateUtil.buildSessionFactory()) {
            //we need to create a proxy for a Session class instance, so it can work in a multithreaded environment.
            var session = (Session) Proxy.newProxyInstance(
                    SessionFactory.class.getClassLoader(),
                    new Class[]{Session.class},
                    (proxy, method, args1)
                            -> method.invoke(sessionFactory.getCurrentSession(), args1)
            );
//            session.beginTransaction();


            var userRepository = new UserRepository(session);
            var companyRepository = new CompanyRepository(session);
            var userCreateMapper = new UserCreateMapper(companyRepository);
            var companyReadMapper = new CompanyReadMapper();
            var userReadMapper = new UserReadMapper(companyReadMapper);
            TransactionInterceptor transactionInterceptor = new TransactionInterceptor(sessionFactory);

            UserService userService = new ByteBuddy()
                    .subclass(UserService.class)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(transactionInterceptor))
                    .make()
                    .load(User.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor(UserRepository.class, UserReadMapper.class, UserCreateMapper.class)
                    .newInstance(userRepository, userReadMapper, userCreateMapper);


//            var maybeUser = userService.findById(1L);
//            maybeUser.ifPresent(System.out::println);

            var id = userService.create(new UserCreateDto(
                    "valera@valera.com",
                    null,
                    PersonalInfo.builder()
                            .firstname("valera")
                            .lastname("Sevostyanov")
//                            .birthDate(LocalDate.of(1988, 1, 1))
                            .build(),
                    null,
                    1));

            System.out.println("id=" + id);

//            session.getTransaction().commit();
        }
    }
}