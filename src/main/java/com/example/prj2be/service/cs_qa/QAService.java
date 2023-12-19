package com.example.prj2be.service.cs_qa;

import com.example.prj2be.domain.board.BoardFile;
import com.example.prj2be.domain.cs_qa.CustomerQA;
import com.example.prj2be.domain.cs_qa.NoticeQaBoardFile;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.board.BoardCommentMapper;
import com.example.prj2be.mapper.cs_qa.NoticeQaBoardFileMapper;
import com.example.prj2be.mapper.cs_qa.QAMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class QAService {

   private final QAMapper mapper;
   private final NoticeQaBoardFileMapper fileMapper;
   private final BoardCommentMapper commentMapper;

   private final S3Client s3;

   @Value("${image.file.prefix}")
   private String urlPrefix;
   @Value("${aws.s3.bucket.name}")
   private String bucket;

   public boolean save(CustomerQA qa, MultipartFile[] files, Member login) throws Exception {
      qa.setQaWriter(login.getId());

      // QABoardFile 테이블에 files 정보저장
      // boardId, name - 어떤 게시물의 파일인지 알아야함.
      int cnt = mapper.insert(qa);

      if (files != null) {
         for (int i = 0; i < files.length; i++) {
            fileMapper.insert(qa.getId(), files[i].getOriginalFilename());

            // 실제 파일 s3 bucket 에 넣어야지만 일단 연습중이니 local에 저장.
            upload(qa.getId(),files[i]);
         }
      }

      return cnt == 1;
   }

   private void upload(Integer fileId, MultipartFile file) throws IOException {

      String key = "prj2/customerQA/" + fileId + "/" + file.getOriginalFilename();

      PutObjectRequest objectRequest = PutObjectRequest.builder()
         .bucket(bucket)
         .key(key)
         .acl(ObjectCannedACL.PUBLIC_READ)
         .build();

      s3.putObject(objectRequest, RequestBody.fromInputStream(
         file.getInputStream(), file.getSize()));
   }

   public boolean validate(CustomerQA qa) {
      if (qa == null) {
         return false;
      }

      if (qa.getQaContent() == null || qa.getQaContent().isBlank()) {
         return false;
      }

      if (qa.getQaTitle() == null || qa.getQaTitle().isBlank()) {
         return false;
      }

      return true;
   }

   public Map<String, Object> qaList(Integer page, String keyword, String filter) {
      Map<String, Object> map = new HashMap<>();
      Map<String, Object> pageInfo = new HashMap<>();

      int countAll = mapper.countAll("%" + keyword + "%", filter);
      int lastPageNumber = (countAll - 1) / 10 + 1;
      int startPageNumber = (page - 1) / 10 * 10 + 1;
      int endPageNumber = startPageNumber + 9;
      endPageNumber = Math.min(endPageNumber, lastPageNumber);
      int prevPageNumber = startPageNumber - 10;
      int nextPageNumber = endPageNumber + 1;

      pageInfo.put("currentPageNumber", page);
      pageInfo.put("startPageNumber", startPageNumber);
      pageInfo.put("endPageNumber", endPageNumber);
      if (prevPageNumber > 0) {
         pageInfo.put("prevPageNumber", prevPageNumber);
      }
      if (nextPageNumber <= lastPageNumber) {
         pageInfo.put("nextPageNumber", nextPageNumber);
      }

      int from = (page - 1) * 10;
      map.put("qaList", mapper.selectAll(from, "%" + keyword + "%", filter));
      map.put("pageInfo", pageInfo);
      return map;
   }

   public CustomerQA get(Integer id) {

      CustomerQA qa = mapper.selectById(id);

      List<NoticeQaBoardFile> noticeQaBoardFiles = fileMapper.selectNamesByFileId(id);

      for (NoticeQaBoardFile noticeQaBoardFile : noticeQaBoardFiles) {
         String url = urlPrefix + "prj2/customerQA/" + id + "/" + noticeQaBoardFile.getFileName();
         noticeQaBoardFile.setUrl(url);
      }

      qa.setFiles(noticeQaBoardFiles);

      return qa;

   }
   public boolean remove(Integer id) {

      commentMapper.deleteByQaBoardId(id);

      deleteFile(id);

      return mapper.deleteById(id) == 1;
   }

   private void deleteFile(Integer id) {
      // 파일명 조회
      List<NoticeQaBoardFile> noticeQaBoardFiles = fileMapper.selectNamesByFileId(id);

      // s3 bucket objects 지우기
      for (NoticeQaBoardFile file : noticeQaBoardFiles) {
         String key = "prj2/customerQA/" + id + "/" + file.getFileName();

         DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

         s3.deleteObject(objectRequest);
      }

      // 첨부파일 레코드 지우기
      fileMapper.deleteByFileId(id);
   }

   public boolean update(CustomerQA qa, List<Integer> removeFileIds, MultipartFile[] uploadFiles) throws IOException {

      if (removeFileIds != null) {
         for (Integer id : removeFileIds) {
            // s3에서 지우기
            NoticeQaBoardFile file = fileMapper.selectById(id);
            String key = "prj2/customerQA/" + qa.getId() + "/" + file.getFileName();
            DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
               .bucket(bucket)
               .key(key)
               .build();
            s3.deleteObject(objectRequest);

            // db에서 지우기
            fileMapper.deleteById(id);
         }
      }

      // 파일 추가하기
      if (uploadFiles != null) {
         for (MultipartFile file : uploadFiles) {
            // s3에 올리기
            upload(qa.getId(), file);
            // db에 추가하기
            fileMapper.insert(qa.getId(), file.getOriginalFilename());
         }
      }

      return mapper.update(qa) == 1;

   }

   public boolean hasAccess(Integer id, Member login) {
      if (login == null) {
         return false;
      }

      if (login.isAdmin()) {
         return true;
      }

      CustomerQA qa = mapper.selectById(id);

      return qa.getQaWriter().equals(login.getId());
   }
}