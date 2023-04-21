package com.yadev.mapper;

import com.yadev.dao.CompanyRepository;
import com.yadev.dto.UserCreateDto;
import com.yadev.entity.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserCreateMapper implements Mapper<UserCreateDto, User> {

    private final CompanyRepository companyRepository;
    @Override
    public User mapFrom(UserCreateDto object) {
        return User.builder()
                .info(object.info())
                .username(object.username())
                .role(object.role())
                .personalInfo(object.personalInfo())
                .company(companyRepository.findById(object  .companyId()).orElseThrow(IllegalArgumentException::new))
                .build();
    }
}
