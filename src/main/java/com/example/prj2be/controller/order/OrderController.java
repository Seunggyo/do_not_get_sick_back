package com.example.prj2be.controller.order;

import com.example.prj2be.domain.order.Orders;
import com.example.prj2be.service.order.OrderWaitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderWaitService orderWaitService;

    @PostMapping("/orderWait")
    public void orderWait(@RequestBody Orders orders) {
        orderWaitService.insert(orders);
    }
}
