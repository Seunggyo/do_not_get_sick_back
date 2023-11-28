package com.example.prj2be.domain.ds;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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
    private Boolean nightCare;
    private String businessLicense;

    // update 란
    private String lat;
    private String lng;
    private String category;


}
