package com.example.prj2be.controller.drug;

import com.example.prj2be.domain.drug.Buy;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.drug.BuyService;
import com.example.prj2be.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drug/buy")
public class DrugBuyController {

    private final BuyService service;
    private final MemberService memberService;

    @GetMapping
    public Member buy(@SessionAttribute(value = "login", required = false) Member login) {

        return memberService.selectById1(login.getId());
    }
}
