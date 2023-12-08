package com.example.prj2be.controller.drug;

import com.example.prj2be.domain.drug.Like;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.drug.DrugLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drug/like")
public class DrugLikeController {

    private final DrugLikeService service;

    @PostMapping
    public ResponseEntity<Map<String, Object>> like(@RequestBody Like like,
                                                    @SessionAttribute(value = "login", required = false) Member login) {

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

       return ResponseEntity.ok(service.update(like, login));

    }

    @GetMapping("drug/{drugId}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Integer drugId,
                                                   @SessionAttribute(value = "login", required = false) Member login) {

        return ResponseEntity.ok(service.get(drugId, login));
    }
}
