package com.example.prj2be.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Member {
    private String id;
    private String password;
    private String nickName;
    private String phone;
    private String email;
    private String address;
    private String auth;
    private LocalDateTime inserted;

    public boolean isAdmin() {
        if (auth == "admin") {
            return true;
        }
        return false;
    }
}


