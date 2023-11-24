package com.example.prj2be.drugStore.controller;

import com.example.prj2be.drugStore.domain.Ds;
import com.example.prj2be.drugStore.service.DsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/ds")
public class DsController {

    private final DsService service;

    @PostMapping("add")
    public ResponseEntity add (@RequestBody Ds ds
                               /*,@SessionAttribute(value = "login", required = false) Member login*/) {
        // 약국 정보 기입

//        if ( login != null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }

        if ( !service.validate(ds)) {
            return  ResponseEntity.badRequest().build();
        }

        if ( service.save(ds)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

}
