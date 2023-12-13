package com.example.prj2be.mapper.order;

import com.example.prj2be.domain.order.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrdersMapper {

    @Insert("""
        insert into orders (orderId, orderName, orderCode, amount, ordererName, ordererPhone,
          ordererEmail, ordererAddress, deliveryName,
           deliveryPhone, deliveryAddress, deliveryComment)
        values (#{orderId}, #{orderName}, #{orderCode}, #{amount}, #{ordererName}, #{ordererPhone},
        #{ordererEmail}, #{ordererAddress}, #{deliveryName},
        #{deliveryPhone}, #{deliveryAddress}, #{deliveryComment})
""")
    int insert(Orders orders);

    @Select("""
        select * from orders
        where ordererName = #{id}
""")
    List<Orders> selectById(String id);
}
