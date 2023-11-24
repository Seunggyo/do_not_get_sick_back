package com.example.prj2be.drugStore.domain;

import lombok.Data;

@Data
public class Ds {
    private Integer id;

    // insert 란
    private String name;
    private String address;
    private String phone;
    private Integer openHour;
    private Integer openMin;
    private Integer closeHour;
    private Integer closeMin;
    private String content;

    // update 란
    private String homePage;
    private String lat;
    private String lng;
    private Boolean nightCare;
    private String category;

    private String file;
}
