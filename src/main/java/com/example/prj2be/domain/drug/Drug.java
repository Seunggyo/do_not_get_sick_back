package com.example.prj2be.domain.drug;

import lombok.Data;

@Data
public class Drug {
    private int id;
    private String name;
    private String function;
    private String content;
    private int price;
}
