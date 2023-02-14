package com.clt.userprofile.repository;

import org.springframework.data.annotation.Id;

import com.clt.userprofile.component.UserProfileEntity;

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

    static UserProfileEntity toEntity(UserProfileModel el) {
        return UserProfileEntity.builder()
                .id(el.getId())
                .userName(el.getUserName())
                .name(el.getName())
                .surname(el.getSurname())
                .description(el.getDescription())
                .build();
    }

    static UserProfileModel fromEntity(UserProfileEntity entity){
        UserProfileModel model = new UserProfileModel();
        model.setId(entity.getId());
        model.setUserName(entity.getUserName());
        model.setName(entity.getName());
        model.setSurname(entity.getSurname());
        model.setDescription(entity.getDescription());
        return model;
    }
}
