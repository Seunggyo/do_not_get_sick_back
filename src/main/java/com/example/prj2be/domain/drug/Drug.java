package com.example.prj2be.domain.drug;

import com.example.prj2be.domain.drug.DrugFile.DrugFile;
import com.example.prj2be.utill.AppUtil;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Drug {
    private int id;
    private String name;
    private String func;
    private String content;
    private Integer price;
    private String shipping;
    private LocalDateTime inserted;
    private String fileName;

    private List<DrugFile> files;

    public String getAgo(){
        return AppUtil.getAgo(inserted);
    }
}
