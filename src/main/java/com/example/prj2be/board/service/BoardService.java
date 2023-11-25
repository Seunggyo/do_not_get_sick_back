package com.example.prj2be.board.service;

import com.example.prj2be.board.board.Board;
import com.example.prj2be.board.mapper.BoardMapper;
import java.lang.reflect.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {


   private final BoardMapper mapper;

   public boolean save(Board board, Member login) {
//      board.setWriter(login.getId());

      return mapper.insert(board) == 1;
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

      if (board.getWriter() == null || board.getWriter().isBlank()) {
         return false;
      }

      return true;
   }

   public List<Board> list() {
      return mapper.selectAll();
   }

   public Board get(Integer id) {
      return mapper.selectById(id);
   }

   public boolean remove(Integer id) {
      return mapper.deleteById(id) == 1;
   }

   public void update(Board board) {
      mapper.update(board);
   }

   public boolean hasAccess(Integer id, Member login) {
      Board board = mapper.selectById(id);

      return board.getWriter().equals(login.getId());
   }
}
