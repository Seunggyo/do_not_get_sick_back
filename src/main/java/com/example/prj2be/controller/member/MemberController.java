package com.example.prj2be.controller.member;

import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService service;

    @GetMapping(value = "check", params = "id")
    public ResponseEntity checkId(String id) {
        if (service.getId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "check", params = "nickName")
    public ResponseEntity checkNickName(String nickName) {
        if (service.getNickName(nickName) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "check", params = "email")
    public ResponseEntity checkEmail(String email) {
        if (service.getEmail(email) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity signup( Member member,
       @RequestParam(value = "uploadFileImg[]",required = false)MultipartFile profile,

       @RequestParam(value = "uploadFile[]",required = false)MultipartFile file
    ) throws IOException {
        if (service.validate(member, file)) {
            if (service.add(member, file, profile)) {
                return ResponseEntity.ok().build();
            } else {
                System.out.println("MemberController.signup");;
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/accept")
    public ResponseEntity accept(@RequestBody Member member) {
        if (service.accept(member)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity cancel(@RequestBody Member member) {
        if (service.cancel(member)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/list")
    public Map<String, Object> memberList(@RequestParam(value = "k",defaultValue = "") String keyword,
                                          @RequestParam(value = "p",defaultValue = "1") Integer page) {

        return service.selectAll(keyword, page);
    }

    @GetMapping("/joinList")
    public Map<String, Object> memberJoinList(@RequestParam(value = "k",defaultValue = "") String keyword,
                                              @RequestParam(value = "p",defaultValue = "1") Integer page) {
        return service.selectJoinAll(keyword, page);
    }

    @GetMapping("/info")
    public Member memberView(String id) {
        //TODO 권한 설정
        System.out.println("id = " + id);
        return service.selectById(id);
    }

    @GetMapping("/login")
    public Member login(@SessionAttribute(value = "login", required = false) Member login) {
        return login;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Member member,
                                WebRequest request) {
        if (service.login(member, request)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("logout")
    public void logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }

    @PutMapping("edit")
    public ResponseEntity edit(@RequestBody Member member,
                               @SessionAttribute(value = "login",required = false)Member login) {
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!service.hasAccess(member.getId(), login)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        if (service.update(member)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/findId")
    public String findId(@RequestBody Member member) {
        return service.findIdByEmail(member.getEmail());
    }
}

