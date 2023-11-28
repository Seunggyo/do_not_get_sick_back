package com.example.prj2be.domain.drug;

import com.example.prj2be.domain.drug.DrugFile.DrugFile;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Drug {
    private int id;
    private String name;
    private String func;
    private String content;
    private Integer price;
    private LocalDateTime inserted;
    private String fileName;

    private List<DrugFile> files;
}
