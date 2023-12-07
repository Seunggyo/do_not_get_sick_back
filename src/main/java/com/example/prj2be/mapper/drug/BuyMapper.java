package com.example.prj2be.mapper.drug;

import com.example.prj2be.domain.drug.Buy;
import com.example.prj2be.domain.drug.Drug;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BuyMapper {

    @Insert("""
            INSERT INTO buy (drugId, drugName, amount, quantity)
            VALUES (#{drugId}, #{drugName}, #{amount}, #{quantity}})    
            """)
    int insert(Buy buy);

    @Select("""
            SELECT b.id,
                   d.id drugId, d.name drugName, d.price * c.quantity amount,
                   m.id memberId, m.nickName, m.phone, m.address,
                   c.quantity
            FROM buy b
            JOIN member m ON b.id = m.id
            JOIN drug d ON b.id = d.id
            JOIN drugCart c ON b.id = c.id
            WHERE b.id = LAST_INSERT_ID();
            """)
    Buy selectBuyById(Integer id);

}
