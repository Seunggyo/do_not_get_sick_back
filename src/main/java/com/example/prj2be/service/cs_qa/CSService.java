package com.example.prj2be.service.cs_qa;

import com.example.prj2be.domain.board.Board;
import com.example.prj2be.domain.board.BoardFile;
import com.example.prj2be.domain.cs_qa.CustomerService;
import com.example.prj2be.domain.cs_qa.NoticeBoardFile;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.cs_qa.CSMapper;
import com.example.prj2be.mapper.cs_qa.NoticeBoardFileMapper;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
public class CSService {

   private final CSMapper mapper;
   private final NoticeBoardFileMapper fileMapper;

   private final S3Client s3;

   @Value("${image.file.prefix}")
   private String urlPrefix;
   @Value("${aws.s3.bucket.name}")
   private String bucket;

   public boolean save(
      CustomerService cs, MultipartFile[] files, Member login) throws IOException {

      cs.setCsWriter(login.getId());

      int cnt = mapper.insert(cs);

      if (files != null) {
         for (int i = 0; i < files.length; i++) {
            fileMapper.insert(cs.getId(), files[i].getOriginalFilename());

            // 실제 파일 s3 bucket 에 넣어야지만 일단 연습중이니 local에 저장.
            upload(cs.getId(),files[i]);
         }
      }

      return cnt == 1;

   }

   private void upload(Integer fileId, MultipartFile file) throws IOException {

      String key = "prj2/CS/" + fileId + "/" + file.getOriginalFilename();

      PutObjectRequest objectRequest = PutObjectRequest.builder()
         .bucket(bucket)
         .key(key)
         .acl(ObjectCannedACL.PUBLIC_READ)
         .build();

      s3.putObject(objectRequest, RequestBody.fromInputStream(
         file.getInputStream(), file.getSize()));
   }

   public boolean validate(CustomerService cs) {
      if (cs == null) {
         return false;
      }

      if (cs.getCsContent() == null || cs.getCsContent().isBlank()) {
         return false;
      }

      if (cs.getCsTitle() == null || cs.getCsTitle().isBlank()) {
         return false;
      }

      return true;
   }

   public Map<String, Object> list(Boolean orderByNum, Boolean orderByHit, Integer page,
      String keyword, String filter) {

      Map<String, Object> map = new HashMap<>();
      Map<String, Object>  pageInfo = new HashMap<>();

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

      // 정렬된 데이터 조회
      List<CustomerService> csList;
      int from = (page - 1) * 10;
      csList = mapper.selectAll(from, "%" + keyword + "%", filter);

      if (orderByNum != null) {
         Comparator<CustomerService> comparator = Comparator.comparing(CustomerService::getId);
         if (orderByNum) {
            comparator = comparator.reversed();
         }
         csList = csList.stream()
            .sorted(comparator)
            .collect(Collectors.toList());
      }

      if (orderByHit != null) {
         Comparator<CustomerService> comparator = Comparator.comparing(CustomerService::getIncreaseHit);
         if (orderByHit) {
            comparator = comparator.reversed();
         }
         csList = csList.stream()
            .sorted(comparator)
            .collect(Collectors.toList());
      }

      map.put("csList", csList);
      map.put("pageInfo", pageInfo);

      return map;
   }

   public CustomerService get(Integer id) {
      CustomerService cs = mapper.selectById(id);

      List<NoticeBoardFile> noticeBoardFiles = fileMapper.selectNamesByFileId(id);

      for (NoticeBoardFile noticeBoardFile: noticeBoardFiles) {
         String url = urlPrefix + "prj2/CS/" + id + "/" + noticeBoardFile.getFileName();
         noticeBoardFile.setUrl(url);
      }

      cs.setFiles(noticeBoardFiles);

      return cs;
   }


   public boolean remove(Integer id) {
      deleteFile(id);

      return mapper.deleteById(id) == 1;
   }

   private void deleteFile(Integer id) {
      // 파일명 조회
      List<NoticeBoardFile> noticeBoardFiles = fileMapper.selectNamesByFileId(id);

      // s3 bucket objects 지우기
      for (NoticeBoardFile file : noticeBoardFiles) {
         String key = "prj2/CS/" + id + "/" + file.getFileName();

         DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

         s3.deleteObject(objectRequest);
      }

      // 첨부파일 레코드 지우기
      fileMapper.deleteByFileId(id);
   }

   public boolean update(CustomerService cs, List<Integer> removeFileIds, MultipartFile[] uploadFiles) throws IOException {

      if (removeFileIds != null) {
         for (Integer id : removeFileIds) {
            // s3에서 지우기
            NoticeBoardFile file = fileMapper.selectById(id);
            String key = "prj2/CS/" + cs.getId() + "/" + file.getFileName();
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
            upload(cs.getId(), file);
            // db에 추가하기
            fileMapper.insert(cs.getId(), file.getOriginalFilename());
         }
      }

      return mapper.update(cs) == 1;

   }

   public boolean hasAccess(Integer id, Member login) {

      if (login == null) {
         return false;
      }

      if (login.getAuth().equals("admin")) {
         return true;
      }

      CustomerService cs = mapper.selectById(id);

      return cs.getCsWriter().equals(login.getId());
   }

   public void csHitCount(int id) {

       mapper.increaseHit(id);

   }
}
