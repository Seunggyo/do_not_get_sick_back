package com.example.prj2be.mapper.board;

import com.example.prj2be.domain.board.BoardLike;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BoardLikeMapper {

   @Delete("""
        DELETE FROM boardLike
        WHERE boardId = #{boardId}
          AND memberId = #{memberId}
        """)
   int delete(BoardLike like);

   @Insert("""
        INSERT INTO boardLike (boardId, memberId)
        VALUES (#{boardId}, #{memberId})
        """)
   int insert(BoardLike like);

   @Select("""
      SELECT COUNT(id) FROM boardLike
      WHERE boardId = #{boardId}
      """)
   int countByBoardId(Integer boardId);

   @Select("""
      SELECT *
      FROM boardLike
      WHERE boardId = #{boardId}
      AND memberId = #{memberId}
      """)
   BoardLike selectByBoardIdAndMemberId(Integer boardId, String memberId);

   @Delete("""
        DELETE FROM boardLike
        WHERE boardId = #{boardId}
        """)
   int deleteByBoardId(Integer boardId);

   @Delete("""
        DELETE FROM boardLike
        WHERE memberId = #{memberId}
        """)
   int deleteByMemberId(String memberId);
}
