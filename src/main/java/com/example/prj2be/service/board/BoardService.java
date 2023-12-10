package com.example.prj2be.service.board;

import com.example.prj2be.domain.board.Board;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.board.BoardCommentMapper;
import com.example.prj2be.mapper.board.BoardFileMapper;
import com.example.prj2be.mapper.board.BoardLikeMapper;
import com.example.prj2be.mapper.board.BoardMapper;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class BoardService {

   private final BoardMapper mapper;
   private final BoardCommentMapper commentMapper;
   private final BoardLikeMapper likeMapper;
   private final BoardFileMapper fileMapper;

   public boolean save(Board board, MultipartFile[] files, Member login) throws IOException {
      board.setWriter(login.getId());

      // boardFile 테이블에 files 정보저장
      // boardId, name - 어떤 게시물의 파일인지 알아야함.
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

   private void upload(Integer boardId, MultipartFile file) throws IOException {

      File folder = new File("C:\\Temp\\prj2\\" + boardId);
      if(!folder.exists()) {
         folder.mkdirs();

         String path = folder.getAbsolutePath() + "\\" + file.getOriginalFilename();
         File des = new File(path);
         file.transferTo(des);
      }
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
      String keyword, Integer countLike) {

      Map<String, Object> map = new HashMap<>();
      Map<String, Object>  pageInfo = new HashMap<>();

      int countAll = mapper.countAll("%" + keyword + "%");
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
      boardList = mapper.selectAll(from, "%" + keyword + "%", countLike);

      // 필요한 경우 정렬을 적용하기
      if (orderByNum != null || orderByHit != null) {
         Comparator<Board> comparator = Comparator.comparing(Board::getId);
         if (orderByNum != null && orderByNum) {
            comparator = comparator.reversed();
         }
         if (orderByHit != null && orderByHit) {
            comparator = comparator.thenComparing(Board::getIncreaseHit).reversed();
         }
         boardList = boardList.stream().sorted(comparator).toList();
      }

      map.put("boardList", boardList);
      map.put("pageInfo", pageInfo);

      return map;
   }
//   public List<Board> list(int countLike) {
//
//      return mapper.selectAll(countLike);
//   }

   public Board get(Integer id) {
      return mapper.selectById(id);
   }

   public boolean remove(Integer id) {
      // 게시물에 달린 댓글들 지우기...
      commentMapper.deleteByBoardId(id);

      // 좋아요 레코드 지우기
      likeMapper.deleteByBoardId(id);

      return mapper.deleteById(id) == 1;
   }

   public boolean update(Board board) {
      return mapper.update(board) == 1;

   }

   public boolean hasAccess(Integer id, Member login) {

      if (login == null) {
         return false;
      }

      if (login.isAdmin()) {
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