package com.example.prj2be.controller.board;

import com.example.prj2be.domain.board.Board;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.board.BoardService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")

public class BoardController {

   private final BoardService service;

   @PostMapping("add")
   public ResponseEntity add(
      Board board,
      @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] files,
      @SessionAttribute(value = "login", required = false) Member login) throws IOException {

      if (login == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
      if (!service.validate(board)) {
         return ResponseEntity.badRequest().build();
      }

      if (service.save(board, files, login)) {
         return ResponseEntity.ok().build();
      } else {
         return ResponseEntity.internalServerError().build();
      }
   }

   @GetMapping("list")
   public Map<String, Object> list(
      @RequestParam(value = "n", required = false) Boolean orderByNum,
      @RequestParam(value = "h", required = false) Boolean orderByHit,
      @RequestParam(value = "p", defaultValue = "1") Integer page,
      @RequestParam(value = "k", defaultValue = "") String  keyword,
      @RequestParam(value = "b",defaultValue = "all") String keywordPop,
      @RequestParam(value = "f",defaultValue = "") String filter) {

      int countLike=0;
      if (keywordPop.equals("pop")) {
         countLike=1;
      }

      return service.list(orderByNum, orderByHit, page, keyword, countLike, "%"+filter+"%");
   }

//   public List<Board> list(@RequestParam(value = "b",defaultValue = "all") String keyword) {
//      int countLike=0;
//      if (keyword.equals("pop")) {
//         countLike=1;
//      }
//      return service.list(countLike);
//   }


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
      Board board,
      @RequestParam(value = "removeFileIds[]", required = false) List<Integer> removeFileIds,
      @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] uploadFiles,
      @SessionAttribute(value = "login", required = false) Member login) throws IOException {

      if (login == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 에러
      }
      if (!service.hasAccess(board.getId(), login)) {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 에러
      }
      if (service.validate(board)) {
         if (service.update(board, removeFileIds, uploadFiles)) {
            return ResponseEntity.ok().build();
         } else {
            return ResponseEntity.internalServerError().build();
         }
      } else {
         return ResponseEntity.badRequest().build();
      }
   }

   @PutMapping("{id}")
   public void hitCount(@PathVariable Integer id) {
      service.hitCount(id);

   }

}
