package com.example.prj2be.controller.board;

import com.example.prj2be.domain.board.Board;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.board.BoardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")

public class BoardController {

   private final BoardService service;

   @PostMapping("add")
   public ResponseEntity add(
      @RequestBody Board board,
      @SessionAttribute(value = "login", required = false) Member login) {
//      System.out.println("login = " + login);

      if (login == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
      if (!service.validate(board)) {
         return ResponseEntity.badRequest().build();
      }

      if (service.save(board, login)) {
         return ResponseEntity.ok().build();
      } else {
         return ResponseEntity.internalServerError().build();
      }
   }

   @GetMapping("list")
   public List<Board> list(@RequestParam(value = "b",defaultValue = "all") String keyword) {
      int likeCount=0;
      if (keyword.equals("pop")) {
         likeCount=1;
      }
      return service.list(likeCount);
   }

   @GetMapping("id/{id}")
   public Board get(@PathVariable Integer id) {
      return service.get(id);
   }

   @DeleteMapping("remove/{id}")
   public ResponseEntity remove(
      @PathVariable Integer id,
      @SessionAttribute(value = "login", required = false) Member login) {

      if (login == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 에러(비로그인상태)
      }
      if (!service.hasAccess(id,login)) {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 에러(본인인지 모름)
      }
      if (service.remove(id)) {
         return ResponseEntity.ok().build();
      } else {
         return ResponseEntity.internalServerError().build();
      }
   }

   @PutMapping("edit")
   public ResponseEntity edit(
      @RequestBody Board board,
      @SessionAttribute(value = "login", required = false) Member login) {

      if (login == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 에러
      }
      if (!service.hasAccess(board.getId(), login)) {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 에러
      }
      if (service.validate(board)) {
         if (service.update(board)) {
            return ResponseEntity.ok().build();
         } else {
            return ResponseEntity.internalServerError().build();
         }
      } else {
         return ResponseEntity.badRequest().build();
      }
   }

}
