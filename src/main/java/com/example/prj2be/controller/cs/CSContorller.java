package com.example.prj2be.controller.cs;


import com.example.prj2be.service.board.BoardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/cs")
@RestController
@RequiredArgsConstructor
public class CSContorller {

   private final CSService service;

   @PostMapping("add")
   public ResponseEntity add(@RequestBody CS cs) {

      if (!service.validate(cs)) {
         return ResponseEntity.badRequest().build();
      }
      if (service.save(cs)) {
         return ResponseEntity.ok().build();
      } else {
         return ResponseEntity.internalServerError().build();
      }
   }

   @GetMapping("list")

   // add 된 게시판 리스트를 보여주는 로직
   public List<CS> list() {
      return service.list();
   }

   @GetMapping("id/{id}")
   public CS get(@PathVariable Integer id) {

      // csDTO 대로 쓴 글을 게시판에 전달..
      return service.get(id);
   }
}
