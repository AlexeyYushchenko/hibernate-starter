//package com.yadev.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@PrimaryKeyJoinColumn(name = "id")
//public class Programmer extends User {
//
//    @Enumerated(EnumType.STRING)
//    private Language language;
//
//    @Builder
//    public Programmer(Long id, Company company, PersonalInfo personalInfo, String username, Role role, Profile profile, String info, List<UserChat> userChats, Language language) {
//        super(id, company, personalInfo, username, role, profile, info, userChats);
//        this.language = language;
//    }
//}
