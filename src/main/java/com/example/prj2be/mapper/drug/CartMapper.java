package com.example.prj2be.mapper.drug;

import com.example.prj2be.domain.drug.Cart;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CartMapper {

    @Delete("""
            DELETE FROM drugCart
            WHERE drugId = #{drugId} 
            AND memberId = #{memberId}
            """)
    int delete(Cart cart);

    @Insert("""
            INSERT INTO drugCart (drugId, memberId, quantity)
            VALUES (#{drugId}, #{memberId}, #{quantity})
            
            """)
    int insert(Cart cart);

    @Select("""
            SELECT COUNT(id) FROM drugCart
            WHERE drugId = #{drugId}
            """)
    int countByDrugId(Integer drugId);

    @Select("""
            SELECT * FROM drugCart
            WHERE drugId = #{drugId}
            AND memberId = #{memberId}
            """)
    Cart selectByDrugIdAndMemberId(Integer drugId, String memberId);

    @Update("""
            UPDATE drugCart
            SET quantity = #{quantity}
            WHERE memberId = #{memberId}
              AND drugId = #{drugId}
            """)
    int updateIncreaseQuantity(Cart cart);
}
