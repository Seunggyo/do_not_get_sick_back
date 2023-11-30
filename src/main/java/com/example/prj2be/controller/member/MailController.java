package com.example.prj2be.controller.member;

import com.example.prj2be.service.member.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @GetMapping("/mail")
    public int mailSend(@RequestParam String email) {
        return mailService.senMail(email);
    }
}
