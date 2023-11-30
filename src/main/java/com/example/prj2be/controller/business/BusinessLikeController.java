package com.example.prj2be.controller.business;

import com.example.prj2be.domain.business.BusinessLike;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.business.BusinessLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/business/like")
public class BusinessLikeController {

    private final BusinessLikeService service;

    @PostMapping
    public ResponseEntity<Map<String, Object>> like(@RequestBody BusinessLike like,
                        @SessionAttribute(value = "login", required = false) Member login) {
        if ( login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(service.update(like, login));
    }

    @GetMapping("dsId/{dsId}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Integer dsId,
                                   @SessionAttribute(value = "login", required = false)Member login) {
        return ResponseEntity.ok(service.get(dsId, login));
    }

}
