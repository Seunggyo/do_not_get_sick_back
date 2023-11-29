package com.example.prj2be.mapper.comment;
import com.example.prj2be.domain.comment.Comment;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommentMapper {

   @Insert("""
      INSERT INTO comment(boardId, comment, memeberId)
      VALUES (#{boardId}, #{comment}, #{memberId})
     
      """)
   int insert(Comment comment);

   @Select("""
      SELECT *
      FROM comment
      WHERE boardId= #{boardId}
      """
   )
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
}
