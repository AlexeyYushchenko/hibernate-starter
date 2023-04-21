package com.yadev.mapper;

import com.yadev.dto.UserReadDto;
import com.yadev.entity.User;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    private final CompanyReadMapper companyReadMapper;
    @Override
    public UserReadDto mapFrom(User user) {
        return new UserReadDto(user.getId(),
                user.getPersonalInfo(),
                user.getUsername(),
                user.getRole(),
                user.getInfo(),
                Optional.ofNullable(user.getCompany())
                        .map(companyReadMapper::mapFrom)
                        .orElse(null));
    }
}
