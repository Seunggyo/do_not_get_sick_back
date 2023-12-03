package com.example.prj2be.controller.drug;

import com.example.prj2be.domain.drug.Cart;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.service.drug.CartService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drug/cart")
public class DrugCartController {


    private final CartService service;

    @PostMapping
    public ResponseEntity<Object> cart(@RequestBody Cart cart,
                                       @SessionAttribute(value = "login", required = false)Member login){

        if (login == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        service.update(cart, login);
        return null;
    }
}
