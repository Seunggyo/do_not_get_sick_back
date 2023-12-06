package com.example.prj2be.domain.ds;

import lombok.Data;

@Data
public class DsKakao {
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
}
