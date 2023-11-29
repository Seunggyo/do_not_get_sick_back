package com.example.prj2be.service.board;

import com.example.prj2be.domain.board.Board;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.board.BoardMapper;
import com.example.prj2be.service.member.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

   private final MemberService memberService;
   private final BoardMapper mapper;

   public boolean save(Board board, Member login) {
      board.setWriter(login.getId());

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

   public boolean update(Board board) {
      return mapper.update(board) == 1;

   }

   public boolean hasAccess(Integer id, Member login) {

//      if (memberService.isAdmin(login)) {
//         return true;
//      }

      Board board = mapper.selectById(id);
      // mapper 에서 해당 게시물정보를 얻기

      return board.getWriter().equals(login.getId());
   }
}