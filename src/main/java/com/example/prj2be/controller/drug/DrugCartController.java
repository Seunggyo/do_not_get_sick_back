package com.example.prj2be.controller.drug;

import com.example.prj2be.domain.drug.Cart;
import com.example.prj2be.service.drug.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/drug/cart")
public class DrugCartController {


    private final CartService service;

    @PostMapping
    public void cart(@RequestBody Cart cart){
        service.update(cart);
    }
}
