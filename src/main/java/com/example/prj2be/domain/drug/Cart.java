package com.example.prj2be.domain.drug;

import lombok.Data;

@Data
public class Cart {
    private Integer id;
    private Integer drugId;
    private String memberId;
    private Integer quantity;
    private String drugName;
    private Integer total;
    private String fileName;
    private String url;
    private String orderCode;

}
