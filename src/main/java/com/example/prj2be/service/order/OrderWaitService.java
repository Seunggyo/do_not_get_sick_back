package com.example.prj2be.service.order;

import com.example.prj2be.domain.order.Orders;
import com.example.prj2be.mapper.order.OrderWaitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderWaitService {

    private final OrderWaitMapper orderWaitMapper;

    public void insert(Orders orders) {
        orderWaitMapper.insert(orders);
    }
}
