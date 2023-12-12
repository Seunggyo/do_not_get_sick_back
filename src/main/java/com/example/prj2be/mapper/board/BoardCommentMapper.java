package com.example.prj2be.mapper.board;

import com.example.prj2be.domain.board.BoardComment;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BoardCommentMapper {
   @Insert("""
      INSERT INTO boardComment(boardId, comment, memberId)
      VALUES (#{boardId}, #{comment}, #{memberId})
     
      """)
   int insert(BoardComment comment);

   @Select("""
      SELECT
          c.id,
          c.comment,
          c.inserted,
          c.boardId,
          c.memberId,
          m.nickName memberNickName
      FROM boardComment c JOIN member m ON c.memberId = m.id
      WHERE c.boardId = #{boardId}
      ORDER BY c.id DESC
      """)
   List<BoardComment> selectByBoardId(Integer boardId);


   @Delete("""
      DELETE FROM boardComment
      WHERE id = #{id}
      """)
   int deleteById(Integer id);

   @Select("""
      SELECT *
      FROM boardComment
      WHERE id = #{id}
      """)
   BoardComment selectById(Integer id);

   @Update("""
      UPDATE boardComment
      SET comment = #{comment}
      WHERE id = #{id}
      """)
   int update(BoardComment comment);

   // 게시물 삭제시 댓글들도 삭제
   @Delete("""
      DELETE FROM boardComment
      WHERE boardId = #{boardId}
      """)
   int deleteByBoardId(Integer boardId);


   // TODO: 멤버탈퇴시 쿼리 넣어야댐..
}
