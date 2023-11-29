package com.example.prj2be.mapper.comment;
import com.example.prj2be.domain.board.Board;
import com.example.prj2be.domain.comment.Comment;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CommentMapper {

   @Insert("""
      INSERT INTO comment(boardId, comment, memeberId)
      VALUES (#{boardId}, #{comment}, #{memberId})
     
      """)
   int insert(Comment comment);

   @Select("""
      SELECT
          c.id,
          c.comment,
          c.inserted,
          c.boardId,
          c.memberId,
          m.nickName memberNickName
      FROM comment c JOIN member m ON c.memberId = m.id
      WHERE boardId = #{boardId}
      ORDER BY c.id DESC
      """)
   List<Comment> selectByBoardId(Integer boardId);


   @Delete("""
      DELETE FROM comment
      WHERE id = #{id}
      """)
   boolean deleteById(Integer id);

   @Select("""
      SELECT *
      FROM comment
      WHERE id = #{id}
      """)
   Comment selectById(Integer id);

   @Update("""
      UPDATE comment
      SET comment = #{comment}
      WHERE id = #{id}
      """)
   int update(Comment comment);
}
