package com.yadev.dao;

import com.yadev.entity.BaseEntity;
import com.yadev.entity.User;
import javax.persistence.EntityManager;

public class UserRepository extends RepositoryBase<Long, User> {
    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }
}
