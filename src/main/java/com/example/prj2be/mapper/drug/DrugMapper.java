package com.example.prj2be.mapper.drug;

import com.example.prj2be.domain.drug.Drug;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DrugMapper {

    @Select("""
        select * from drug
        where function=#{function}
            """)
    List<Drug> selectByFunction(String function);

    @Insert("""
            INSERT INTO drug (name,function,content,price,inserted )
            VALUES (#{name}, #{func},#{content},#{price},#{inserted})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Drug drug);
}
