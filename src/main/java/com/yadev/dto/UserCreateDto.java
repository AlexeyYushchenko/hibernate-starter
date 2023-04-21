package com.yadev.dto;

import com.yadev.entity.PersonalInfo;
import com.yadev.entity.Role;
import com.yadev.validation.UpdateGroup;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public record UserCreateDto(
        @Email
        String username,
        @NotNull(groups = UpdateGroup.class)
        Role role,
        @Valid
        PersonalInfo personalInfo,
        String info,
        Integer companyId) {

}
