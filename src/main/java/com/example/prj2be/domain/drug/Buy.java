package com.example.prj2be.domain.drug;

import lombok.Data;

@Data
public class Buy {

    private Integer id;
    private String memberId;
    private String nickName;
    private String phone;
    private String address;
    private Integer drugId;
    private String drugName;
    private Integer amount;
    private Integer quantity;
}
