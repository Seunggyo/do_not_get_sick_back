package com.example.prj2be.service.order;

import com.example.prj2be.domain.drug.Cart;
import com.example.prj2be.domain.order.Orders;
import com.example.prj2be.mapper.order.OrderListMapper;
import com.example.prj2be.mapper.order.OrdersMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrdersMapper ordersMapper;
    private final OrderListMapper orderListMapper;
    @Value("${image.file.prefix}")
    private String urlPrefix;

    public List<Orders> selectById(String id) {
        List<Orders> ordersList = ordersMapper.selectById(id);
        for (Orders order : ordersList) {
            String url = urlPrefix + "prj2/drug/" + order.getDrugId() + "/" + order.getFileName();
            order.setUrl(url);
        }

        return ordersList;
    }

    public List<Cart> orderLIst(String orderId) {
        List<Cart> orderList = orderListMapper.selectByOrderId(orderId);
        for (Cart order : orderList) {
            String url = urlPrefix + "prj2/drug/" + order.getDrugId() + "/" + order.getFileName();
            order.setUrl(url);
        }
        return orderList;
    }

    public void deleteByOrderId(String orderId) {
        ordersMapper.deleteByOrderId(orderId);
        orderListMapper.deleteByOrderId(orderId);
    }
}
