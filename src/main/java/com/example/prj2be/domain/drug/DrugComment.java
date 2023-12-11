package com.example.prj2be.domain.drug;

import com.example.prj2be.domain.drug.DrugFile.DrugFile;
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
}
