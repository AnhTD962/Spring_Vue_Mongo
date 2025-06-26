package com.example.backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class User {

    @Id
    private String id;

    private String name;

    @Field("mobile_number")
    private String mobileNumber;

    private String email;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String password;

    @Field("profile_image")
    private String profileImage;

    private String role;

    @Field("is_enable")
    private Boolean isEnable;

    @Field("account_non_locked")
    private Boolean accountNonLocked;

    @Field("failed_attempt")
    private Integer failedAttempt;

    @Field("lock_time")
    private Date lockTime;

    @Field("reset_token")
    private String resetToken;

}
