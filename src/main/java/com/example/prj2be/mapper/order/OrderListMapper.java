package com.example.prj2be.mapper.order;

import com.example.prj2be.domain.drug.Cart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderListMapper {
    @Insert("""
        insert into orderList (memberId, drugId, quantity, orderId)
        values (#{cart.memberId}, #{cart.drugId}, #{cart.quantity}, #{orderId})
""")
    void insertByCart(Cart cart, String orderId);

    @Select("""
        SELECT distinct 
            c.id, d.name drugName, 
            c.quantity, d.price*c.quantity total,
            d.id drugId, d.`function` func, c.memberId,
            (select name from drugFile where drugId = d.id limit 1) fileName
            FROM orderList c
            JOIN drug d ON d.id = c.drugId
            join drugFile df on d.id = df.drugId
            where c.orderId = #{orderId}
""")
    List<Cart> selectByOrderId(String orderId);

    @Delete("""
        delete from orderList
        where orderId = #{orderId}
""")
    void deleteByOrderId(String orderId);
}
