package com.example.prj2be.domain.drug;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Drug {
    private int id;
    private String name;
    private String func;
    private String content;
    private Integer price;
    private LocalDateTime inserted;
    private String fileName;
}
