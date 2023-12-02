package com.example.prj2be.service.drug;

import com.example.prj2be.domain.comment.Comment;
import com.example.prj2be.domain.drug.Drug;
import com.example.prj2be.domain.drug.DrugFile.DrugFile;
import com.example.prj2be.mapper.drug.DrugMapper;
import com.example.prj2be.mapper.drug.FileMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


import java.io.IOException;
import java.util.List;
import java.util.Stack;

@Service
@RequiredArgsConstructor
public class DrugService {

    private final DrugMapper mapper;
    private final FileMapper fileMapper;

    private final S3Client s3;
    @Value("${image.file.prefix}")
    private String urlPrefix;
    @Value("${aws.s3.bucket.name}")
    private String bucket;

    public List<Drug> selectByFunction(String function) {
        return mapper.selectByFunction(function);
    }

    public boolean save(Drug drug, MultipartFile[] files) throws IOException {

        int cdt = mapper.insert(drug);

        //drugFile 테이블에 files 정보 저장.
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                // drugId, name
                fileMapper.insert(drug.getId(), files[i].getOriginalFilename());

                upload(drug.getId(), files[i]);
            }
        }

        // 파일을 s3 bucket에 upload
        return cdt == 1;
    }

    private void upload(Integer drugId, MultipartFile file) throws IOException {

        String key = "prj2/drug/" + drugId + "/" + file.getOriginalFilename();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }

    public boolean validate(Drug drug) {
        if (drug == null) {
            return false;
        }
        if (drug.getName() == null || drug.getName().isBlank()) {
            return false;
        }
        if (drug.getFunc() == null || drug.getFunc().isBlank()) {
            return false;
        }
        if (drug.getPrice() == null || drug.getPrice() == 0) {
            return false;
        }
        if (drug.getContent() == null || drug.getContent().isBlank()) {
            return false;
        }


        return true;
    }

    public List<Drug> drugList(Integer page) {

        int from = (page - 1) * 6;

        List<Drug> drugList = mapper.selectDrugList(from);

        for (Drug drug : drugList) {

            List<DrugFile> drugFiles = fileMapper.selectNamesByDrugId(drug.getId());

            for (DrugFile drugFile : drugFiles) {
                String url = urlPrefix + "prj2/drug/" + drug.getId() + "/" + drugFile.getName();
                drugFile.setUrl(url);
            }
            drug.setFiles(drugFiles);
        }
        return drugList;
    }

    public Drug drugGet(Integer id) {

        Drug drug = mapper.selectById(id);

        List<DrugFile> drugFiles = fileMapper.selectNamesByDrugId(id);

        for (DrugFile drugFile : drugFiles) {
            String url = urlPrefix + "prj2/drug/" + id + "/" + drugFile.getName();
            drugFile.setUrl(url);
        }

        drug.setFiles(drugFiles);
        return drug;
    }

    public boolean remove(Integer id) {

        deleterFile(id);

        return mapper.deleteById(id) == 1;
    }

    public void deleterFile(Integer id){

      List<DrugFile> drugFiles = fileMapper.selectNamesByDrugId(id);

      for (DrugFile file : drugFiles) {
          String key = "prj2/drug/" + id + "/" + file.getName();

          DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                  .bucket(bucket)
                  .key(key)
                  .build();
          s3.deleteObject(objectRequest);
      }
      fileMapper.deleteByBoardId(id);
    }


    public boolean update(Drug drug, List<Integer> removeFileIds, MultipartFile[] uploadFiles ) throws IOException {

        // 파일 지우기
        if (removeFileIds != null) {
            for (Integer id : removeFileIds){
                // s3 지우기
                DrugFile file = fileMapper.selectById(id);
                String key = "prj2/drug" + drug.getId() + "/" + file.getName();
                DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build();
                s3.deleteObject(objectRequest);

                fileMapper.deleteById(id);
            }
        }

        if (uploadFiles != null) {
            for (MultipartFile file :  uploadFiles) {

                upload(drug.getId(), file);

                fileMapper.insert(drug.getId(), file.getOriginalFilename());
            }
        }

        System.out.println("drug = " + drug);
        return mapper.update(drug) == 1;
    }

}
