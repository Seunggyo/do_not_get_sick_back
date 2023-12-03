package com.example.prj2be.mapper.drug;

import com.example.prj2be.domain.drug.Cart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CartMapper {

    @Delete("""
            DELETE FROM drugCart
            WHERE drugId = #{drugId} 
            AND memberId = #{memberId}
            """)
    int delete(Cart cart);

    @Insert("""
            INSERT INTO drugCart (drugId, memberId)
            VALUES (#{drugId}, #{memberId})
            
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
    Cart selectByDrugIdAndMemberId(Integer drugId, String id);
}
