package com.example.prj2be.domain.drug;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DrugComment {
    private Integer id;
    private Integer drugId;
    private String memberId;
    private String memberNickName;
    private String comment;
    private LocalDateTime inserted;
}
