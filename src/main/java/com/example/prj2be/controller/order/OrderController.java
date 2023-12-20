package com.example.prj2be.controller.order;

import com.example.prj2be.domain.drug.Cart;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.domain.order.Orders;
import com.example.prj2be.service.order.OrderService;
import com.example.prj2be.service.order.OrderWaitService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
import org.springframework.http.ResponseEntity;
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
    public List<Orders> history(@SessionAttribute(value = "login", required = false) Member login) {
        return orderService.selectById(login.getId());
    }

    @GetMapping("/orderList")
    public List<Cart> orderList(@RequestParam String orderId) {
        return orderService.orderLIst(orderId);
    }

    @DeleteMapping("/remove")
    public ResponseEntity Remove(String orderId) {
        orderService.deleteByOrderId(orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/historyAll")
    public List<Orders> historyAll(@SessionAttribute(value = "login", required = false) Member login) {
        return orderService.selectByAll(login.getId());
    }
}
