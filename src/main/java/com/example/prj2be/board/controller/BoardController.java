package com.example.prj2be.board.controller;


import com.example.prj2be.board.board.Board;
import com.example.prj2be.board.service.BoardService;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")

public class BoardController {

   private final BoardService service;

   @PostMapping("add")
   public ResponseEntity add(@RequestBody Board board) {
      if (!service.validate(board)) {
         return ResponseEntity.badRequest().build();
      }

      if (service.save(board)) {
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
   public ResponseEntity remove(@PathVariable Integer id) {

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
