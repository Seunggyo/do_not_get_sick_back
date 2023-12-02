package com.example.prj2be.domain.ds;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DsComment {
    private Integer id;
    private Integer businessId;
    private String memberId;
    private String comment;
    private LocalDateTime inserted;

}
