package com.example.prj2be.domain.hs;

import java.util.List;
import lombok.Data;

@Data
public class Hs {

    private Integer id;
    private String memberId;
    private String name;
    private String address;
    private String phone;
    private Integer openHour;
    private Integer openMin;
    private Integer restHour;
    private Integer restMin;
    private Integer closeHour;
    private Integer closeMin;
    private String content;
    private String lat;
    private String lng;
    private String category;
    private List<HsCourse> medicalCourse;
    private String homePage;
    private Integer countLike;
    private List<HsFile> files;
    private Boolean nightCare;


}
