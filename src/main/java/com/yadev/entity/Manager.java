//package com.yadev.entity;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.DiscriminatorValue;
//import javax.persistence.Entity;
//import javax.persistence.PrimaryKeyJoinColumn;
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@PrimaryKeyJoinColumn(name = "id")
//public class Manager extends User {
//
//    private String projectName;
//
//    @Builder //this will allow to also use the fields of the extended User class.
//    public Manager(Long id, Company company, PersonalInfo personalInfo, String username, Role role, Profile profile, String info, List<UserChat> userChats, String projectName) {
//        super(id, company, personalInfo, username, role, profile, info, userChats);
//        this.projectName = projectName;
//    }
//}
