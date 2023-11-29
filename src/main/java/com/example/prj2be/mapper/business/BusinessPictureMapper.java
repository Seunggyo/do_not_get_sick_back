package com.example.prj2be.mapper.business;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BusinessPictureMapper {

    @Insert("""
            INSERT INTO businesspicture (businessId,name)
            VALUES (#{businessId},#{name} )
            """)
    int insert(Integer businessId, String name );
}
