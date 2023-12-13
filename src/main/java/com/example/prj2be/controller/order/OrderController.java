package com.example.prj2be.controller.order;

import com.example.prj2be.domain.member.Member;
import com.example.prj2be.domain.order.Orders;
import com.example.prj2be.service.order.OrderService;
import com.example.prj2be.service.order.OrderWaitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderWaitService orderWaitService;
    private final OrderService orderService;

    @PostMapping("/orderWait")
    public void orderWait(@RequestBody Orders orders) {
        orderWaitService.insert(orders);
    }

    @GetMapping("/history")
    public List<Orders> history(@SessionAttribute(value = "login",required = false)Member login) {
        return orderService.selectById(login.getId());
    }
}
