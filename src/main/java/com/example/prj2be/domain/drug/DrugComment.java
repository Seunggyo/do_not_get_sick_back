package com.example.prj2be.domain.drug;

import com.example.prj2be.domain.drug.DrugFile.DrugFile;
import com.example.prj2be.utill.AppUtil;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DrugComment {
    private Integer id;
    private Integer drugId;
    private String memberId;
    private String memberNickName;
    private String comment;
    private LocalDateTime inserted;

    private List<DrugFile> files;

    public String getAgo(){
        return AppUtil.getAgo(inserted);
    }
}
