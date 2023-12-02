package com.example.prj2be.domain.ds;

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
    private String content;
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
