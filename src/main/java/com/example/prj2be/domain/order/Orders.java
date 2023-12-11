package com.example.prj2be.domain.order;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Orders {
    private Integer orderNumber;
    private String orderId;
    private String productName;
    private Integer quantity;
    private Long amount;
    private String orderName;

    private String ordererName;
    private String ordererPhone;
    private String ordererEmail;
    private String ordererAddress;

    private String deliveryName;
    private String deliveryPhone;
    private String deliveryAddress;
    private String deliveryComment;
    private LocalDateTime inserted;
}
