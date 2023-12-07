package com.example.prj2be.controller.ds;

import com.example.prj2be.domain.ds.DsLike;
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
public class DsLikeController {

    private final BusinessLikeService service;

    @PostMapping
    public ResponseEntity<Map<String, Object>> like(@RequestBody DsLike like,
                        @SessionAttribute(value = "login", required = false) Member login) {

        if ( login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(service.update(like, login));
    }

    @GetMapping("dsId/{id}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Integer id,
                                   @SessionAttribute(value = "login", required = false)Member login) {
        return ResponseEntity.ok(service.get(id, login));
    }

    @GetMapping("dsName/{name}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable String name,
                       @SessionAttribute(value = "login", required = false)Member login) {

        Integer id = service.getIdByName(name);

        return ResponseEntity.ok(service.get(id, login));
    }

}
