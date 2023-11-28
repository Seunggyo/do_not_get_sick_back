package com.example.prj2be.controller.ds;

import com.example.prj2be.domain.ds.DsLike;
import com.example.prj2be.service.ds.DsLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ds/like")
public class DsLikeController {

    private final DsLikeService service;

    @PostMapping
    public ResponseEntity like (DsLike like){
        return ResponseEntity.ok(service.update(like));
    }

}
