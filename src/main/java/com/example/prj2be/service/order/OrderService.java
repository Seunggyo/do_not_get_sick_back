package com.example.prj2be.service.order;

import com.example.prj2be.domain.order.Orders;
import com.example.prj2be.mapper.order.OrdersMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrdersMapper ordersMapper;

    public List<Orders> selectById(String id) {
        return ordersMapper.selectById(id);
    }
}
