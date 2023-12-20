package com.example.prj2be.controller.hs;

import com.example.prj2be.domain.hs.HsComment;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.hs.HsCommentService;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hospital/comment")
public class HsCommentController {

    private final HsCommentService service;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody HsComment comment,
        @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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
    public Map<String, Object> list(@RequestParam("id") Integer businessId,
                                    @RequestParam(value = "p", defaultValue = "1") Integer page) {
        return service.list(businessId, page);
    }

    @PutMapping("edit")
    public ResponseEntity update(@RequestBody HsComment comment,
        @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (service.hasAccess(comment.getId(), login)) {
            if (!service.updateValidate(comment)) {
                return ResponseEntity.badRequest().build();
            }
            if (service.update(comment)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> remove(@PathVariable Integer id,
        @SessionAttribute(value = "login", required = false) Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (service.hasAccess(id, login)) {
            if (service.remove(id)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
