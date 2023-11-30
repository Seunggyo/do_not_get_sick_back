package com.example.prj2be.domain.member;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Member {
    private Integer id;
    private String password;
    private String nickName;
    private String birthday;
    private String phone;
    private String email;
    private String address;
    private String auth;
    private String fileName;
    private LocalDateTime inserted;

    public boolean isAdmin() {
        if (auth == "admin") {
            return true;
        }
        return false;
    }
}


