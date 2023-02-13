package com.clt.userprofile.repository;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserProfileModel {
    @Id
    Long id;
    String userName;
    String name;
    String surname;
    String description;
}
