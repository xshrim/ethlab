package com.szyz.rock.model.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "user")
public class UserInfo {

    @Field("username")
    private String userName;

}
