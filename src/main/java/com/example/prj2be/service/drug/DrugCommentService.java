package com.example.prj2be.service.drug;

import com.example.prj2be.domain.drug.DrugComment;
import com.example.prj2be.domain.drug.DrugFile.DrugFile;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.drug.DrugCommentMapper;
import com.example.prj2be.mapper.drug.FileMapper;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DrugCommentService {

    private final DrugCommentMapper mapper;
    private final FileMapper fileMapper;

    private final S3Client s3;
    @Value("${image.file.prefix}")
    private String urlPrefix;
    @Value("${aws.s3.bucket.name}")
    private String bucket;

    public boolean add(DrugComment drugComment, MultipartFile[] files, Member login)
        throws IOException {

        drugComment.setMemberId(login.getId());

        int cnt = mapper.insert(drugComment);

        // drugCommentFile 테이블에 files 정보 저장
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                // drugCommentId, FileName
                fileMapper.CommentInsert(drugComment.getId(), files[i].getOriginalFilename());

                // 실제 파일을 S3 bucket에 upload
                // 일단 local에 저장
                upload(drugComment.getId(), files[i]);
            }
        }
        return cnt == 1;
    }

    private void upload(Integer commentId, MultipartFile file) throws IOException {

        String key = "prj2/drug1/" + commentId + "/" + file.getOriginalFilename();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .acl(ObjectCannedACL.PUBLIC_READ)
            .build();

        s3.putObject(objectRequest,
            RequestBody.fromInputStream(file.getInputStream(), file.getSize()));


    }

    public boolean validate(DrugComment drugComment) {

        if (drugComment == null) {
            return false;
        }
        if (drugComment.getDrugId() == null || drugComment.getDrugId() < 1) {
            return false;
        }
        if (drugComment.getComment() == null || drugComment.getComment().isBlank()) {
            return false;
        }
        return true;
    }

    public List<DrugComment> list(Integer drugId) {

        List<DrugComment> drugCommentList = mapper.selectByDrugId(drugId);

        for (DrugComment drugComment : drugCommentList) {

            List<DrugFile> drugFiles = fileMapper.selectNamesByDrugComment(drugComment.getId());

            for (DrugFile drugFile : drugFiles) {
                String url =
                    urlPrefix + "prj2/drug1/" + drugComment.getId() + "/" + drugFile.getName();
                drugFile.setUrl(url);
            }
            drugComment.setFiles(drugFiles);
        }
        return drugCommentList;
    }

    public boolean remove(Integer id) {

        deleteFile(id);

        return mapper.deleteById(id) == 1;

    }

    private void deleteFile(Integer id) {
        //파일명 조회
        List<DrugFile> drugFiles = fileMapper.selectNamesByDrugComment(id);

        // s3 bucket objects 지우기
        for (DrugFile file : drugFiles) {
            String key = "prj2/drug1/" + id + "/" + file.getName();

            DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(bucket)
                .build();

            s3.deleteObject(objectRequest);
        }

        // 첨부파일 레코드 지우기
        fileMapper.deleteByCommentId(id);
    }


    public boolean hasAccess(Integer id, Member login) {
        if (login == null) {
            return false;
        }
        if (login.getAuth().equals("admin")) {
            return true;
        }
        DrugComment drugComment = mapper.selectById(id);

        return drugComment.getMemberId().equals(login.getId());
    }


    public boolean update(DrugComment comment, List<Integer> removeFileIsd,
        MultipartFile[] uploadFiles) throws IOException {

        //파일 지우기
        if (removeFileIsd != null) {
            for (Integer id : removeFileIsd) {
                //s3 에서 지우기
                DrugFile file = fileMapper.selectByCommentId(id);
                if (file != null) {

                    String key = "prj2/drug1/" + comment.getId() + "/" + file.getName();
                    DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build();
                    s3.deleteObject(objectRequest);

                    //db에서 지우기
                    fileMapper.deleteByCommentId(id);
                }
            }
        }

        //파일 추가히기
        if (uploadFiles != null) {
            for (MultipartFile file : uploadFiles) {
                //s3에 올리기
                upload(comment.getId(), file);
                //db에 추가하기
//            fileMapper.insert(comment.getId(), file.getOriginalFilename());
                fileMapper.insertCommentFile(comment.getId(), file.getOriginalFilename());
            }
        }

        return mapper.update(comment) == 1;
    }

    public boolean updateValidate(DrugComment comment) {
        if (comment == null) {
            return false;
        }

        if (comment.getId() == null) {
            return false;
        }
        if (comment.getComment() == null || comment.getComment().isBlank()) {
            return false;
        }
        return true;
    }
}
