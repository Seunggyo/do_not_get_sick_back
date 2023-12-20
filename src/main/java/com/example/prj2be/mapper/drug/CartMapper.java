package com.example.prj2be.mapper.drug;

import com.example.prj2be.domain.drug.Buy;
import com.example.prj2be.domain.drug.Cart;
import org.apache.ibatis.annotations.*;

import java.util.List;

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

    @Select("""
            SELECT distinct 
            c.id, d.name drugName, 
            c.quantity, d.price*c.quantity total,
            d.id drugId, d.`function` func, c.memberId,
            (select name from drugFile where drugId = d.id limit 1) fileName
            FROM drugCart c
            JOIN drug d ON d.id = c.drugId
            join drugFile df on d.id = df.drugId
            where c.memberId = #{memberId}
            """)
    List<Cart> selectCartList(String memberId);

    @Delete("""
            DELETE FROM drugCart
            WHERE id = #{id}

            """)
    int deleteById(Integer id);

    @Select("""
        select c.memberId, m.nickName, m.phone, m.address,
        c.drugId, c.quantity, d.price, m.email
        from drugCart c
        join member m on m.id = c.memberId
        join drug d on d.id = c.drugId
        where c.id = #{id}
""")
    Buy selectBuyById(Integer id);

    @Delete("""
        delete from drugCart
        where memberId = #{memberId}
""")
    void deleteByMemberId(String memberId);

    @Delete("""
        delete from buy
        where memberId = #{memberId}
""")
    void deleteBuyByMemberId(String memberId);
}