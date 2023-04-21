package com.yadev.interceptor;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class TransactionInterceptor {

    private final SessionFactory sessionFactory;

    @RuntimeType
    public Object intercept(@SuperCall Callable<Object> callable, @Origin Method method) throws Exception {
        Transaction transaction = null;
        boolean isTransactionStarted = false;
        if (method.isAnnotationPresent(Transactional.class)) {
            transaction = sessionFactory.getCurrentSession().getTransaction();
            if (!transaction.isActive()) {
                transaction.begin();
                isTransactionStarted = true;
            }
        }

        Object result = null;
        try {
            result = callable.call();
            if (isTransactionStarted) {
                transaction.commit();
            }
        } catch (Exception e) {
            if (isTransactionStarted) {
                transaction.rollback();
            }
            throw e;
        }


        return result;
    }
}
