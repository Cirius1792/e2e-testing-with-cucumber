package com.clt.userprofile.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.clt.userprofile.component.UserProfileEntity;

import jakarta.annotation.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@Table("USER_PROFILE")
public class UserProfileModel {
    @Id
    Long id;
    String username;
    String name;
    String surname;
    String description;

    static UserProfileEntity toEntity(UserProfileModel el) {
        return UserProfileEntity.builder()
                .id(el.getId())
                .userName(el.getUsername())
                .name(el.getName())
                .surname(el.getSurname())
                .description(el.getDescription())
                .build();
    }

    static UserProfileModel fromEntity(UserProfileEntity entity){
        UserProfileModel model = new UserProfileModel();
        model.setId(entity.getId());
        model.setUsername(entity.getUserName());
        model.setName(entity.getName());
        model.setSurname(entity.getSurname());
        model.setDescription(entity.getDescription());
        return model;
    }
}
