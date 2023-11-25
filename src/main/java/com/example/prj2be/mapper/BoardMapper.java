package com.example.prj2be.mapper;

import com.example.prj2be.domain.Board;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
      SELECT id, title, content, writer, category, inserted
      FROM board
      WHERE id = #{id}
      """)
   Board selectById(Integer id);


   @Delete("""
      DELETE FROM board
      WHERE id = #{id}
      """)
   int deleteById(Integer id);

   @Update("""
      UPDATE board
      SET 
      title = #{title},
      content = #{content},
      writer = #{writer},
      category = #{category}
     
      WHERE id = #{id}
      """)
   int update(Board board);
}
