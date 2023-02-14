package com.clt.userprofile.router.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateUserRequest {
    String userName;
    String name;
    String surname;
    String description;    
}
