package com.example.prj2be.service.ds;

import com.example.prj2be.domain.ds.DsPicture;
import com.example.prj2be.domain.ds.Ds;
import com.example.prj2be.mapper.business.BusinessPictureMapper;
import com.example.prj2be.mapper.ds.DsMapper;
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

    public boolean update(Ds ds, MultipartFile[] uploadFile, List<Integer> deleteFileIds) throws IOException {
        // 유저가 정보 수정 할려 할 떄 보내는 코드

        // 파일 삭제
        if (deleteFileIds != null) {
            for (Integer id : deleteFileIds) {
                DsPicture picture = businessFileMapper.selectById(id);
                String key = "prj2/Ds/" + ds.getId() + "/" + picture.getName();
                DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build();
                s3.deleteObject(objectRequest);

                // db에서 삭제
                businessFileMapper.deleteById(id);
            }
        }

        // 파일 업데이트
        if ( uploadFile != null) {
            // 미리 작성한 upload를 사용하여 aws 저장
            for (MultipartFile file : uploadFile){
                upload(ds.getId(), file);
                // db에 추가
                businessFileMapper.insert(ds.getId(), file.getOriginalFilename());
            }
        }

        return mapper.updateById(ds) == 1;
    }

    public List<Ds> list( ) {
        return mapper.selectByCategory();
    }

    public Ds get(Integer id) {
        Ds ds = mapper.selectById(id);

        List<DsPicture> dsPictures = businessFileMapper.selectNamesByDsId(id);

        for (DsPicture dsPicture : dsPictures){
            String url = urlPrefix + "prj2/Ds/" + id + "/" + dsPicture.getName();
            dsPicture.setUrl(url);
        }

        ds.setFiles(dsPictures);

        return ds;
    }


    public boolean delete(Integer id) {


        // 파일 레코드 삭제
        businessFileMapper.deleteByDsId(id);


        return mapper.deleteById(id) == 1;
    }
}
