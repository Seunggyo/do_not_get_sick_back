package com.example.prj2be.domain.ds;

import com.example.prj2be.utill.AppUtil;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@Data
public class DsComment {
    private Integer id;
    private Integer businessId;
    private String memberId;
    private String memberNickName;
    private String comment;
    private LocalDateTime inserted;

    public String getAgo(){
        return AppUtil.getAgo(inserted);
    }

}
