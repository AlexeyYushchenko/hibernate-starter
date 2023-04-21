package com.yadev.service;

import com.yadev.dao.UserRepository;
import com.yadev.dto.UserCreateDto;
import com.yadev.dto.UserReadDto;
import com.yadev.entity.User;
import com.yadev.mapper.Mapper;
import com.yadev.mapper.UserCreateMapper;
import com.yadev.mapper.UserReadMapper;
import com.yadev.validation.UpdateGroup;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateMapper userCreateMapper;

    @Transactional
    public Long create(UserCreateDto userCreateDto){
        //validation
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        var validator = validatorFactory.getValidator();
        var validationResult = validator.validate(userCreateDto, UpdateGroup.class);
        if (!validationResult.isEmpty()){
            throw new ConstraintViolationException(validationResult);
        }
        //map
        var userEntity = userCreateMapper.mapFrom(userCreateDto);
        return userRepository.save(userEntity).getId();
    }
    @Transactional
    public <T> Optional<T> findById(Long id, Mapper<User, T> mapper) {
        Map<String, Object> properties = Map.of(
                GraphSemantic.LOAD.getJpaHintName(),
                userRepository.getEntityManager().getEntityGraph("user-company"));
        return userRepository.findById(id, properties)
                .map(mapper::mapFrom);
    }

    @Transactional
    public Optional<UserReadDto> findById(Long id) {
        return findById(id, userReadMapper);
    }

    @Transactional
    public boolean delete(Long id) {
        var user = userRepository.findById(id);
        user.ifPresent(u -> userRepository.delete(u.getId()));
        return user.isPresent();
    }
}
