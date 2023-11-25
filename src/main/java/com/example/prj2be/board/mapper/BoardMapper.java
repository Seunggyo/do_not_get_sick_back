package com.example.prj2be.board.mapper;

import com.example.prj2be.board.board.Board;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BoardMapper {

   @Insert("""
      INSERT INTO board(title, content, writer)
      VALUES (#{title}, #{content}, #{writer})
      """)
   int insert(Board board);

   @Select("""
      SELECT id, title, writer, category
      FROM board
      ORDER BY id DESC
      """)
   List<Board> selectAll();

   @Select("""
      SELECT id, title, content, writer, category
      FROM board
      WHERE id = #{id}
      """)
   Board selectById(Integer id);
}
