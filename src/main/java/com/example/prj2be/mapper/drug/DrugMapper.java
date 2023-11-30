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
            INSERT INTO drug (name,function,content,price)
            VALUES (#{name}, #{func},#{content},#{price})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Drug drug);


    @Select("""
            select DISTINCT d.id, d.name, d.function func, d.content, d.price, d.inserted
            FROM drug d
            JOIN drugFile f
            ON d.id = f.drugId
            
            """)
    List<Drug> selectDrugList();

    @Select("""
            select d.id, d.name, d.function func, d.content, d.price, d.inserted
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
            price = #{price}
            WHERE id = #{id}
            """)
    int update(Drug drug);
}
