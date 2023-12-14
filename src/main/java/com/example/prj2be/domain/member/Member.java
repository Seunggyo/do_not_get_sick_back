package com.example.prj2be.domain.member;

import lombok.Data;

import java.time.LocalDateTime;
import software.amazon.awssdk.services.s3.endpoints.internal.Value.Str;

@Data
public class Member {
    private String id;
    private String password;
    private String nickName;
    private String birthday;
    private String phone;
    private String email;
    private String address;
    private String auth;
    private String fileName;
    private String profile;
    private String url;

    private LocalDateTime inserted;

    public boolean isAdmin() {
        if (auth == "admin") {
            return true;
        }
        return false;
    }
}


