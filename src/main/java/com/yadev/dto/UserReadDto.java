package com.yadev.dto;

import com.yadev.entity.PersonalInfo;
import com.yadev.entity.Role;
import lombok.Value;

public record UserReadDto(Long id,
                          PersonalInfo personalInfo,
                          String username,
                          Role role,
                          String info,
                          CompanyReadDto company) {

}
