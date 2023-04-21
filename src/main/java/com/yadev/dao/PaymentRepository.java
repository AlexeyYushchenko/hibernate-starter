package com.yadev.dao;

import com.yadev.entity.Payment;

import javax.persistence.EntityManager;

public class PaymentRepository extends RepositoryBase<Long, Payment> {
    public PaymentRepository(EntityManager entityManager){
        super(Payment.class, entityManager);
    }
}
