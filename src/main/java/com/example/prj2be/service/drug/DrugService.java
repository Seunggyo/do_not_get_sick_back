package com.example.prj2be.service.drug;

import com.example.prj2be.domain.drug.Drug;
import com.example.prj2be.mapper.drug.DrugMapper;
import com.example.prj2be.mapper.drug.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;


import java.io.File;
import java.util.List;
@Service
@RequiredArgsConstructor
public class DrugService {

    private final DrugMapper mapper;
    private final FileMapper fileMapper;

    public List<Drug> selectByFunction(String function) {
       return mapper.selectByFunction(function);
    }

    public boolean save(Drug drug, MultipartFile[] files) {

        int cdt = mapper.insert(drug);

        //drugFile 테이블에 files 정보 저장.
        if ( files != null){
            for (int i = 0; i < files.length; i++) {
                // drugId, name
                fileMapper.insert(drug.getId(), files[i].getOriginalFilename());

                upload(drug.getId(),files[i]);
            }
        }

        // 파일을 s3 bucket에 upload
        return cdt == 1;
    }

    private void upload(int drugId, MultipartFile file) {

        try{
            File folder = new File("C:\\Temp\\prj2\\" + drugId);
            if (!folder.exists()){
                folder.mkdirs();
            }

            String path = folder.getAbsolutePath() + "\\" + file.getOriginalFilename();
            File des = new File(path);
            file.transferTo(des);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validate(Drug drug) {
        if (drug == null) {
            return false;
        }
        if (drug.getName() == null || drug.getName().isBlank()){
            return false;
        }
        if (drug.getFunc() == null || drug.getFunc().isBlank()){
            return false;
        }
        if (drug.getPrice() == null || drug.getPrice() == 0){
           return false;
       }
        if (drug.getContent() == null || drug.getContent().isBlank()){
           return false;
       }
       return true;
    }

    public List<Drug> drugList() {
        return mapper.selectDrugList();
    }

    public Drug drugGet(Integer id) {
        return mapper.selectById(id);
    }
}
