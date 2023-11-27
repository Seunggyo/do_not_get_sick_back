package com.example.prj2be.domain;

import lombok.Data;

@Data
public class Hs {

    private Integer id;
    private String name;
    private String address;
    private String phone;
    private Integer openHour;
    private Integer openMin;
    private Integer closeHour;
    private Integer closeMin;
    private String content;
    private String lat;
    private String lng;
    private String category;
    private String homePage;
    private Integer nightCare;


}
