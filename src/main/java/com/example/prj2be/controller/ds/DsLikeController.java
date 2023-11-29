package com.example.prj2be.controller.ds;

import com.example.prj2be.domain.business.BusinessLike;
import com.example.prj2be.service.business.BusinessLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ds/like")
public class DsLikeController {

    private final BusinessLikeService service;

    @PostMapping
    public ResponseEntity like (BusinessLike like){
        return ResponseEntity.ok(service.update(like));
    }

}
