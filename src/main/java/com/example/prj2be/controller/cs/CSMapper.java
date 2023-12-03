package com.example.prj2be.controller.cs;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CSMapper {

   @Insert("""
      INSERT INTO cs(csTitle, csContent, csWriter)
      VALUES (#{title}, #{content, #{writer})
      """)
   int insert(CS cs);

   @Select("""
      SELECT id, csTitle, csWriter, instered
      FROM cs
      ORDER BY id DESC 
      """)
   List<CS> selectAll();


   @Select("""
      SELECT *
      FROM cs
      WHERE id= #{id}
      """)
   CS selectById(Integer id);
}
