package com.example.prj2be.service.board;

import com.example.prj2be.domain.board.BoardComment;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.board.BoardCommentMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardCommentService {

   private final BoardCommentMapper mapper;

   public boolean add(BoardComment comment, Member login) {
      comment.setMemberId(login.getId());
      return mapper.insert(comment) == 1;
   }

   public boolean validate(BoardComment comment) {
      if (comment == null) {
         return false;
      }

      if (comment.getBoardId() == null || comment.getBoardId() < 1) {
         return false;
      }

      if (comment.getComment() == null || comment.getComment().isBlank()) {
         return false;
      }

      return true;
   }

   public List<BoardComment> list(Integer boardId) {
      return mapper.selectByBoardId(boardId);
   }

   public boolean remove(Integer id) {
      return mapper.deleteById(id) == 1;
   }


   public boolean hasAccess(Integer id, Member login) {
      BoardComment comment = mapper.selectById(id);

      return comment.getMemberId().equals(login.getId());
   }

   public boolean update(BoardComment comment) {
      return mapper.update(comment) == 1;
   }

   public boolean updateValidate(BoardComment comment) {
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
