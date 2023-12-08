package com.example.prj2be.mapper.board;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardFileMapper {

   @Insert("""
      INSERT INTO boardFile (boardId, name)
      VALUES (#{boardId}, #{name})
      """)
   int insert(Integer baordId, String name);
}
