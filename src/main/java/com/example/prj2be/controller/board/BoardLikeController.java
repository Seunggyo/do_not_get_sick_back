package com.example.prj2be.controller.board;

import com.example.prj2be.domain.board.BoardLike;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.board.BoardLikeService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class BoardLikeController {

   private final BoardLikeService service;

   @PostMapping
   public ResponseEntity<Map<String, Object>> like(
      // Map의 키,밸류에 키는 list가 false인지 true인지
      // 밸류에는 countLike좋아요개수가 몇개인지를 저장시켜준다..
      @RequestBody BoardLike like,
      @SessionAttribute(value = "login", required = false) Member login) {

      if (login == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }

      return ResponseEntity.ok(service.update(like, login));
   }
}
