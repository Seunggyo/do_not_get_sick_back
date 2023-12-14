package com.example.prj2be.controller.board;

import com.example.prj2be.domain.board.BoardComment;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.board.BoardCommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class BoardCommentController {

   private final BoardCommentService service;

   @PostMapping("add")
   public ResponseEntity add(
      @RequestBody BoardComment comment,
      @SessionAttribute(value = "login", required = false) Member login) {

      if (login == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }

      if (service.validate(comment)) {
         if (service.add(comment, login)) {
            return ResponseEntity.ok().build();
         } else {
            return ResponseEntity.internalServerError().build();
         }
      } else {
         return ResponseEntity.badRequest().build();
      }
   }

   @GetMapping("list")
   public List<BoardComment> list(
      @RequestParam("id") Integer boardId,
      @RequestParam("category") String category) {
      return service.list(boardId, category);
   }

   @DeleteMapping("{id}")
   public ResponseEntity remove(
      @PathVariable Integer id,
      @SessionAttribute(value = "login", required = false) Member login) {

      if (login == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 에러
      }

      if (service.hasAccess(id, login)) {
         if (service.remove(id)) {
            return ResponseEntity.ok().build();
         } else {
            return ResponseEntity.internalServerError().build();
         }
      } else {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 에러
      }
   }
   @PutMapping("edit")
   public ResponseEntity update(@RequestBody BoardComment comment,
      @SessionAttribute(value = "login", required = false) Member login) {
      if (login == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }

      if (service.hasAccess(comment.getId(), login)) {
         if (!service.updateValidate(comment)) {
            return ResponseEntity.badRequest().build();
         }

         if (service.update(comment)) {
            return ResponseEntity.ok().build();
         } else {
            return ResponseEntity.internalServerError().build();
         }
      } else {

         return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
   }
}
