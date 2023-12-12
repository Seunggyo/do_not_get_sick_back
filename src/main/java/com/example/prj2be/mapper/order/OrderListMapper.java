package com.example.prj2be.mapper.order;

import com.example.prj2be.domain.drug.Cart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderListMapper {
    @Insert("""
        insert into orderList (memberId, drugId, quantity, orderId)
        values (#{cart.memberId}, #{cart.drugId}, #{cart.quantity}, #{orderId})
""")
    void insertByCart(Cart cart, String orderId);
}
