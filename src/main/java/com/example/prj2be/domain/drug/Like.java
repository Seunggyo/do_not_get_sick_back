package com.example.prj2be.domain.drug;

import lombok.Data;

@Data
public class Like {

    private Integer id;
    private Integer drugId;
    private Integer memberId;
}
