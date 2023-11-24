package com.example.prj2be.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class Ds {
    private Integer id;

    // insert 란
    private String name;
    private String address;
    private String phone;
    private String openHour;
    private String openMin;
    private String closeHour;
    private String closeMin;
    private String content;
    private Boolean nightCare;

    // update 란
    private String lat;
    private String lng;
    private String category;

    private MultipartFile file;
}
