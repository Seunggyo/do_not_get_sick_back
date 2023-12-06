package com.example.prj2be.controller.ds;

import com.example.prj2be.domain.ds.DsComment;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.ds.DsCommentService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ds/comment")
public class DsCommentController {

    private final DsCommentService service;

    @PostMapping("add")
    public ResponseEntity add (@RequestBody DsComment comment,
                               @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

//        TODO : validate 쪽 문제가 심각함 아마도 businessId랑 연관되있는듯 전체적으로 수정 필요할뜻
        if (service.validate(comment)) {
            if (service.add(comment, login)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("list")
    public List<DsComment> list (@RequestParam("id") Integer businessId) {
        return service.list(businessId);
    }

    @GetMapping("listName")
    public List<DsComment> listName (@RequestParam("name") String memberId) {
        return service.listName(memberId);
    }

    @PutMapping("edit")
    public boolean edit(@RequestBody DsComment comment) {

        return service.update(comment);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id){
        service.delete(id);
    }
}
