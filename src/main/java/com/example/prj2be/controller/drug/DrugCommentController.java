package com.example.prj2be.controller.drug;


import com.example.prj2be.domain.drug.DrugComment;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.drug.DrugCommentService;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drug/comment")
public class DrugCommentController {

    private final DrugCommentService service;

    @PostMapping("add")
    public ResponseEntity add(DrugComment drugComment,
                              @RequestParam(value = "uploadFiles[]", required = false) MultipartFile[] files,
                              @SessionAttribute(value = "login", required = false) Member login) throws IOException {
        
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (service.validate(drugComment)) {
            if (service.add(drugComment, files, login)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("list")
    public List<DrugComment> list(@RequestParam("id") Integer drugId){
        return service.list(drugId);
    }

    @DeleteMapping("{id}")
    public ResponseEntity remove(@PathVariable Integer id,
                                 @SessionAttribute(value = "login", required = false) Member login) {
//        TODO: 관리자 권한도 추가 해야함.
        System.out.println("id = " + id);

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); //401
        }

        if (service.hasAccess(id, login)) {
            if (service.remove(id)) {
                return ResponseEntity.ok().build(); //200
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); //403 권한 없음
        }
    }

    @PutMapping("edit")
    public ResponseEntity update(DrugComment comment,
                                 @RequestParam(value = "removeFileIds[]", required = false)List<Integer> removeFileIds,
                                 @RequestParam(value = "uploadFiles[]", required = false)MultipartFile[] uploadFiles,
                                 @SessionAttribute(value = "login", required = false) Member login) throws IOException {

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (service.hasAccess(comment.getId(), login)) {
            if (!service.updateValidate(comment)) {
                return ResponseEntity.badRequest().build();
            }
            if (service.update(comment, removeFileIds, uploadFiles)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}