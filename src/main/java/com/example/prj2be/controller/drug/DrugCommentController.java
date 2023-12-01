package com.example.prj2be.controller.drug;

import com.example.prj2be.domain.comment.Comment;
import com.example.prj2be.domain.drug.DrugComment;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.drug.DrugCommentService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drug/comment")
public class DrugCommentController {

    private final DrugCommentService service;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody DrugComment drugComment,
                              @SessionAttribute(value = "login", required = false) Member login) {

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (service.validate(drugComment)) {
            if (service.add(drugComment, login)) {
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
    public void remove(@PathVariable Integer id){
//        TODO: 권한 검증
        service.remove(id);
    }
}
