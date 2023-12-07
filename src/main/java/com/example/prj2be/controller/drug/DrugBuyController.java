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

    @PostMapping("add/{id}")
    public ResponseEntity add(@RequestBody Buy buy,
                              @SessionAttribute(value = "login", required = false)Member login) {

      if (login == null) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
      return ResponseEntity.ok(service.save(buy, login));

    }

    @GetMapping("id/{id}")
    public Buy buy(@PathVariable Integer id){
        return service.getBuy(id);
    }
}
