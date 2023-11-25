package com.example.prj2be.controller;

import com.example.prj2be.domain.Board;
import com.example.prj2be.domain.Member;
import com.example.prj2be.service.BoardService;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")

public class BoardController {

   private final BoardService service;

   @PostMapping("add")
   public ResponseEntity add(
      @RequestBody Board board,
      @SessionAttribute(value = "login", required = false) Member login) {
//      System.out.println("board = " + board);

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
   public List<Board> list() {
      return service.list();
   }

   @GetMapping("id/{id}")
   public Board get(@PathVariable Integer id) {
      return service.get(id);
   }

   @DeleteMapping("remove/{id}")
   public ResponseEntity remove(
      @PathVariable Integer id,
      @SessionAttribute(value = "login") Member login) {

      if (login == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
      if (!service.hasAccess(id, login)) {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
      if (service.remove(id)) {
         return ResponseEntity.ok().build();
      } else {
         return ResponseEntity.internalServerError().build();
      }
   }

   @PutMapping("edit")
   public void edit(@RequestBody Board board) {
      service.update(board);
   }

}
