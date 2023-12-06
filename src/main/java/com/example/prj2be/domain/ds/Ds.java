package com.example.prj2be.domain.ds;

import com.example.prj2be.domain.business.BusinessHoliday;
import lombok.Data;

import java.util.List;

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
    private Integer restHour;
    private Integer restMin;
    private Integer restCloseHour;
    private Integer restCloseMin;
    private List<BusinessHoliday> holidays;
    private String content;
    private String info;
    private Boolean nightCare;
    private List<DsPicture> files;

    // update 란
    private String lat;
    private String lng;
    private String category;

    // 좋아요, 댓글
    private Integer likeCount;
    private Integer commentCount;

}
