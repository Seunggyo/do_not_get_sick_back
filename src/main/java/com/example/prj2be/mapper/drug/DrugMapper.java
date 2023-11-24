package com.example.prj2be.mapper.drug;

import com.example.prj2be.domain.drug.Drug;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DrugMapper {

    @Select("""
        select * from drug
        where function=#{function}
            """)
    List<Drug> selectByFunction(String function);
}
