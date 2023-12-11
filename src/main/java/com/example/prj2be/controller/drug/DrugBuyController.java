package com.example.prj2be.controller.drug;

import com.example.prj2be.domain.drug.Buy;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.drug.BuyService;
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

    @GetMapping("id/{id}")
    public Buy buy(@PathVariable Integer id){
        return service.getBuy(id);
    }
}
