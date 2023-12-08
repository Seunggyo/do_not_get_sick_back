package com.example.prj2be.controller.drug;

import com.example.prj2be.domain.drug.Like;
import com.example.prj2be.service.drug.DrugLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drug/like")
public class DrugLikeController {

    private final DrugLikeService service;

    @PostMapping
    public void like(@RequestBody Like like){
        service.update(like);
    }
}
