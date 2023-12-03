package com.example.prj2be.mapper.drug;

import com.example.prj2be.domain.drug.Drug;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DrugMapper {

    @Select("""
            select * from drug
            where function=#{function}
                """)
    List<Drug> selectByFunction(String function);

    @Insert("""
            INSERT INTO drug (name, function, content, price, shipping)
            VALUES (#{name}, #{func},#{content},#{price}, #{shipping})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Drug drug);


    @Select("""
            select DISTINCT d.id, d.name, d.function func, d.content, d.price, d.inserted, d.shipping
            FROM drug d
            JOIN drugFile f
            ON d.id = f.drugId
            ORDER BY d.id DESC 
            LIMIT #{from}, 6
            """)
    List<Drug> selectDrugList(Integer from);

    @Select("""
            select d.id, d.name, d.function func, d.content, d.price, d.inserted, d.shipping
            FROM drug d
            WHERE d.id = #{id}
            """)
    Drug selectById(Integer id);

    @Delete("""
            DELETE FROM drug
            WHERE id = #{id}
            """)
    int deleteById(Integer id);

    @Update("""
            UPDATE drug
            SET 
            name = #{name},
            function = #{func},
            content = #{content},
            price = #{price},
            shipping = #{shipping}
            WHERE id = #{id}
            """)
    int update(Drug drug);
}
