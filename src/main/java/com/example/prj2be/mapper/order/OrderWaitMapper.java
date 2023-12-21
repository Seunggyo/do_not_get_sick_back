package com.example.prj2be.mapper.order;

import com.example.prj2be.domain.order.Orders;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderWaitMapper {


    @Insert("""
        insert into orderWait (orderId, orderName, orderCode, amount, ordererName, ordererPhone,
          ordererEmail, ordererAddress, deliveryName,
           deliveryPhone, deliveryAddress, deliveryComment)
        values (#{orderId}, #{orderName}, #{orderCode}, #{amount}, #{ordererName}, #{ordererPhone},
        #{ordererEmail}, #{ordererAddress}, #{deliveryName},
        #{deliveryPhone}, #{deliveryAddress}, #{deliveryComment})
""")
    void insert(Orders orders);

    @Select("""
        select * from orderWait
        where orderId = #{orderId}
""")
    Orders seletByOrderId(String orderId);

    @Delete("""
        delete from orderWait
        where orderId = #{orderId}
""")
    void deleteByOrderId(String orderId);

    @Delete("""
        delete from orderWait
        where ordererName = #{memberId}
""")
    void deleteByMemberId(String memberId);
}