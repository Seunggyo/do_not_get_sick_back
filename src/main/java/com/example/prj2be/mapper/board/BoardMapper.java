package com.example.prj2be.mapper.board;

import com.example.prj2be.domain.board.Board;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BoardMapper {

   @Insert("""
      INSERT INTO board(title, content, writer,category)
      VALUES (#{title}, #{content}, #{writer}, #{category})
      """)
   int insert(Board board);

   @Select("""
      SELECT b.id,
               b.title,
               b.writer,
               m.nickName,
               b.category,
               b.inserted,
               COUNT(c.id) countComment
        FROM board b JOIN member m ON b.writer = m.id
                     LEFT JOIN boardComment c on b.id = c.boardId
        GROUP BY b.id
        ORDER BY b.id DESC
      """)
   List<Board> selectAll();

   @Select("""
      SELECT b.id, b.title, b.content, b.writer, m.nickName, b.category, b.inserted
      FROM board b JOIN member m ON b.writer = m.id
      WHERE b.id = #{id}
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

   @Delete("""
        DELETE FROM board
        WHERE writer = #{writer}
        """)

   int deleteByWriter(String writer);

}
