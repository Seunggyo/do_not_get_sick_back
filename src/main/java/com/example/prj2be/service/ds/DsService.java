package com.example.prj2be.service.ds;

import com.example.prj2be.domain.ds.Ds;
import com.example.prj2be.mapper.business.BusinessPictureMapper;
import com.example.prj2be.mapper.ds.DsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DsService {

    private final DsMapper mapper;
    private final BusinessPictureMapper businessFileMapper;
    private final S3Client s3;

    @Value("${image.file.prefix}")
    private String urlPrefix;
    @Value("${aws.s3.bucket.name}")
    private String bucket;


    public boolean validate(Ds ds) {
        // 약국 기입시 필수적으로 적어야 하는 목록

        if ( ds == null ){
            return false;
        }
        if (ds.getName().isBlank()) {
            return false;
        }
        if (ds.getAddress().isBlank()) {
            return false;
        }
        if (ds.getPhone().isBlank()) {
            return false;
        }
        if (ds.getContent().isBlank()) {
            return false;
        }

        return true;
    }

    public boolean save(Ds ds, MultipartFile[] files) throws IOException {
        // 올바르게 접근한 사용자가 정보 저장 시 db로 정보 보내는 코드

        int cnt = mapper.insert(ds);
        System.out.println(ds.getId());
        System.out.println(ds);

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                businessFileMapper.insert(ds.getId(), files[i].getOriginalFilename());

                upload(ds.getId(), files[i]);
            }
        }

        return cnt == 1;
    }

    private void upload(Integer dsId, MultipartFile file) throws IOException {
//         aws 저장 코드
        String key = "prj2/Ds/" + dsId + "/" + file.getOriginalFilename();
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

    }

    public boolean update(Ds ds) {
        // 유저가 정보 수정 할려 할 떄 보내는 코드
        return mapper.updateById(ds) == 1;
    }

    public List<Ds> list( ) {
        return mapper.selectByCategory();
    }

    public Ds get(Integer id) {
        return mapper.selectById(id);
    }


    public boolean delete(Integer id) {
        return mapper.deleteById(id) == 1;
    }
}
