package com.example.prj2be.mapper.business;

import com.example.prj2be.domain.business.BusinessPicture;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BusinessPictureMapper {

    @Insert("""
            INSERT INTO businesspicture (businessId,name)
            VALUES (#{businessId},#{name} )
            """)
    int insert(Integer businessId, String name );

    @Select("""
            SELECT id, name
            FROM businesspicture
            WHERE businessId = #{id}
            """)
    List<BusinessPicture> selectNamesByDsId(Integer id);
}
