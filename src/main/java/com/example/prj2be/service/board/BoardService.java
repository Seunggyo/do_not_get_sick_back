package com.example.prj2be.service.board;

import com.example.prj2be.domain.board.Board;
import com.example.prj2be.domain.board.BoardFile;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.board.BoardCommentMapper;
import com.example.prj2be.mapper.board.BoardFileMapper;
import com.example.prj2be.mapper.board.BoardLikeMapper;
import com.example.prj2be.mapper.board.BoardMapper;
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
public class BoardService {

   private final BoardMapper mapper;
   private final BoardCommentMapper commentMapper;
   private final BoardLikeMapper likeMapper;
   private final BoardFileMapper fileMapper;

   private final S3Client s3;

   @Value("${image.file.prefix}")
   private String urlPrefix;
   @Value("${aws.s3.bucket.name}")
   private String bucket;

   public boolean save(Board board, MultipartFile[] files, Member login) throws IOException {
      board.setWriter(login.getId());

      // Id, name - 어떤 게시물의 파일인지 알아야함.
      int cnt = mapper.insert(board);

      if (files != null) {
         for (int i = 0; i < files.length; i++) {
            fileMapper.insert(board.getId(), files[i].getOriginalFilename());

            // 실제 파일 s3 bucket 에 넣어야지만 일단 연습중이니 local에 저장.
            upload(board.getId(),files[i]);
         }
      }

      return cnt == 1;
   }

   private void upload(Integer fileId, MultipartFile file) throws IOException {

      String key = "prj2/board/" + fileId + "/" + file.getOriginalFilename();

      PutObjectRequest objectRequest = PutObjectRequest.builder()
         .bucket(bucket)
         .key(key)
         .acl(ObjectCannedACL.PUBLIC_READ)
         .build();

      s3.putObject(objectRequest, RequestBody.fromInputStream(
         file.getInputStream(), file.getSize()));
   }

   public boolean validate(Board board) {
      if (board == null) {
         return false;
      }

      if (board.getContent() == null || board.getContent().isBlank()) {
         return false;
      }

      if (board.getTitle() == null || board.getTitle().isBlank()) {
         return false;
      }

      return true;
   }

   public Map<String, Object> list(Boolean orderByNum, Boolean orderByHit, Integer page,
      String keyword, Integer popCount, String filter) {

      Map<String, Object> map = new HashMap<>();
      Map<String, Object>  pageInfo = new HashMap<>();

      int countAll = mapper.countAll("%" + keyword + "%", popCount, filter);
      int lastPageNumber = (countAll - 1) / 10 + 1;
      int startPageNumber = (page - 1) / 10 * 10 + 1;
      int endPageNumber = startPageNumber + 9;
      endPageNumber = Math.min(endPageNumber, lastPageNumber);
      int prevPageNumber = Math.max(startPageNumber - 10, 1);
      int nextPageNumber = Math.min(endPageNumber + 1, lastPageNumber);

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
      List<Board> boardList;
      int from = (page - 1) * 10;
      boardList = mapper.selectAll(from, "%" + keyword + "%", popCount, filter);

      // 필요한 경우 정렬을 적용하기
      if (orderByNum != null) {
         Comparator<Board> comparator = Comparator.comparing(Board::getId);
         if (orderByNum) {
            comparator = comparator.reversed();
         }
         boardList = boardList.stream()
            .sorted(comparator)
            .collect(Collectors.toList());
      }

      if (orderByHit != null) {
         Comparator<Board> comparator = Comparator.comparing(Board::getIncreaseHit);
         if (orderByHit) {
            comparator = comparator.reversed();
         }
         boardList = boardList.stream()
            .sorted(comparator)
            .collect(Collectors.toList());
      }


      map.put("boardList", boardList);
      map.put("pageInfo", pageInfo);

      return map;
   }

   public Board get(Integer id) {
      Board board = mapper.selectById(id);

      List<BoardFile> boardFiles = fileMapper.selectNamesByFileId(id);

      for (BoardFile boardFile : boardFiles) {
         String url = urlPrefix + "prj2/board/" + id + "/" + boardFile.getFileName();
         boardFile.setUrl(url);
      }

      board.setFiles(boardFiles);

      return board;
   }

   public boolean remove(Integer id) {
      // 게시물에 달린 댓글들 지우기...
      commentMapper.deleteByBoardId(id);

      // 좋아요 레코드 지우기
      likeMapper.deleteByBoardId(id);

      deleteFile(id);

      return mapper.deleteById(id) == 1;
   }

   private void deleteFile(Integer id) {
      // 파일명 조회
      List<BoardFile> boardFiles = fileMapper.selectNamesByFileId(id);

      // s3 bucket objects 지우기
      for (BoardFile file : boardFiles) {
         String key = "prj2/board/" + id + "/" + file.getFileName();

         DeleteObjectRequest objectRequest = DeleteObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

         s3.deleteObject(objectRequest);
      }

      // 첨부파일 레코드 지우기
      fileMapper.deleteByFileId(id);
   }

   public boolean update(Board board, List<Integer> removeFileIds, MultipartFile[] uploadFiles) throws IOException {

      if (removeFileIds != null) {
         for (Integer id : removeFileIds) {
            // s3에서 지우기
            BoardFile file = fileMapper.selectById(id);
            String key = "prj2/board/" + board.getId() + "/" + file.getFileName();
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
            upload(board.getId(), file);
            // db에 추가하기
            fileMapper.insert(board.getId(), file.getOriginalFilename());
         }
      }

      return mapper.update(board) == 1;

   }

   public boolean hasAccess(Integer id, Member login) {

      if (login == null) {
         return false;
      }

      if (login.getAuth().equals("admin")) {
         return true;
      }

      Board board = mapper.selectById(id);
      // mapper 에서 해당 게시물정보를 얻기

      return board.getWriter().equals(login.getId());
   }

   public void hitCount(int id) {

      mapper.increaseHit(id);

   }

}