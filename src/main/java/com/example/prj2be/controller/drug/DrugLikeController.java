package com.example.prj2be.controller.drug;

import com.example.prj2be.domain.drug.Like;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.drug.DrugLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drug/druglike")
public class DrugLikeController {

    private final DrugLikeService service;

    @PostMapping
    public ResponseEntity like(@RequestBody Like like,
                               @SessionAttribute(value = "login", required = false)Member login){

        if (login == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        service.update(like, login);
        return null;
    }
}
