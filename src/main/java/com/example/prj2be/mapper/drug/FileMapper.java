package com.example.prj2be.mapper.drug;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {

    @Insert("""
            INSERT INTO drugFile (drugId, name)
            VALUES (#{drugId}, #{name})
            """)
    int insert(Integer drugId, String name);
}
