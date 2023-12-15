package com.example.prj2be.mapper.board;

import com.example.prj2be.domain.board.Board;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BoardMapper {

   @Insert("""
      INSERT INTO board(
      title, content, writer,category)
      VALUES (#{title}, #{content}, #{writer}, #{category})
      """)
   @Options(useGeneratedKeys = true, keyProperty = "id")
   int insert(Board board);

   @Select("""  
      SELECT   b.id,
               b.title,
               b.category,
               b.writer,
               m.nickName,
               b.inserted,
               b.increaseHit,
               COUNT(DISTINCT c.id)       countComment,
               COUNT(DISTINCT l.memberId) countLike,
               COUNT(DISTINCT f.id)       countFile
     FROM board b
               LEFT JOIN boardFile f 
                  ON b.id = f.fileId
               JOIN member m
                  ON b.writer = m.id
               LEFT JOIN boardComment c
                  ON b.id = c.boardId   
               and c.category = "board"
               LEFT JOIN boardLike l 
                  ON b.id = l.boardId
     WHERE     (b.category    LIKE #{keyword}
               OR b.title     LIKE #{keyword}
               OR m.nickName  Like #{keyword})
               and b.category like #{filter}
     GROUP BY b.id
     having count(distinct l.memberId) >= #{popCount}
     ORDER BY b.id DESC
     LIMIT #{from}, 10
        """)
   List<Board> selectAll(Integer from, String keyword, Integer popCount, String filter);

   @Select("""
      SELECT b.id, 
      b.title, 
      b.content, 
      b.writer, 
      m.nickName, 
      b.category, 
      b.inserted
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
      SET   title = #{title},
            content = #{content},
            writer = #{writer},
            category = #{category}
      WHERE id = #{id}
      """)
   int update(Board board);


//   @Delete("""
//        DELETE FROM board
//        WHERE writer = #{writer}
//        """)
//
//   int deleteByWriter(String writer);

   @Select("""
        SELECT COUNT(b.id) FROM board b
        WHERE (b.title LIKE #{keyword}
           OR b.content LIKE #{keyword}
           OR b.category Like #{keyword})
           and b.category like #{filter}
           and (select count(*) 
                from boardLike l 
                where l.boardId = b.id) >= #{popCount}
        """)
   int countAll(String keyword, Integer popCount, String filter);

   @Update("""
      UPDATE board
      SET increaseHit = increaseHit + 1
      WHERE id = #{id}
      """)
   void increaseHit(int id);

}
