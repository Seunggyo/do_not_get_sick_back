package com.example.prj2be.mapper.hs;

import com.example.prj2be.domain.hs.HsFile;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HsFileMapper {

    @Insert("""
        INSERT INTO prj2.businesspicture(businessId,name)
        VALUES (#{id},#{originalFilename})
        """)
    int insert(Integer id, String originalFilename);

    @Select("""
        SELECT *
        FROM prj2.businesspicture
        WHERE Id = #{id}
        """)
    HsFile selectById(Integer id);

    @Delete("""
        DELETE FROM prj2.businesspicture
        WHERE id = #{id}
        """)
    int deleteById(Integer id);

    @Select("""
        SELECT id,name
        FROM prj2.businesspicture
        WHERE businessId = #{businessId}
        """)
    List<HsFile> selectByHsId(Integer businessId);

    @Delete("""
        DELETE FROM prj2.businesspicture
        WHERE businessId = #{businessId}
        """)
    void deleteByHsId(Integer businessId);

}
