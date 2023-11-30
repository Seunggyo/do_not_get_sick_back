package com.example.prj2be.controller.hs;

import com.example.prj2be.domain.hs.HsLike;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.hs.HsLikeService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hospital/like")
public class HsLikeController {

    private final HsLikeService service;

    @PostMapping
    public ResponseEntity<Map<String, Object>> like(@RequestBody HsLike like,
        @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().body(service.update(like, login));
    }

    @GetMapping("hospital/{id}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Integer id,
        @SessionAttribute(value = "login", required = false) Member login) {
        return ResponseEntity.ok(service.get(id, login));
    }
}
