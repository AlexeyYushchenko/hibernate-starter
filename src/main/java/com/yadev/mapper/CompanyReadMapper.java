package com.yadev.mapper;

import com.yadev.dto.CompanyReadDto;
import com.yadev.entity.Company;

public class CompanyReadMapper implements Mapper<Company, CompanyReadDto> {
    @Override
    public CompanyReadDto mapFrom(Company c) {
        return new CompanyReadDto(c.getId(), c.getName(), c.getLocales());
    }
}
