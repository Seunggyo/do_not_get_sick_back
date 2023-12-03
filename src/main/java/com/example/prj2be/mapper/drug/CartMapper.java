package com.example.prj2be.mapper.drug;

import com.example.prj2be.domain.drug.Cart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

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
}
