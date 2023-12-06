package com.example.prj2be.domain.business;

import lombok.Data;

@Data
public class BusinessHoliday {
    private Integer id;
    private Integer businessId;
    private String holiday;
}
