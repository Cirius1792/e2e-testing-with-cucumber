package com.clt.userprofile.component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder 
@RequiredArgsConstructor
public class UserProfileEntity {
    final Long id;
    final String userName;
    final String name;
    final String surname;
    final String description;
}
