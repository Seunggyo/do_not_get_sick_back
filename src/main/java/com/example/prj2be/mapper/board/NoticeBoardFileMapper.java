package com.example.prj2be.mapper.board;

import com.example.prj2be.domain.board.NoticeBoardFile;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface NoticeBoardFileMapper {

   @Insert("""
      INSERT INTO NoticeBoardFile (fileId, fileName)
      VALUES (#{fileId}, #{fileName})
      """)

   int insert(Integer fileId, String fileName);

   @Select("""
        SELECT id, fileName
        FROM noticeBoardFile
        WHERE fileId = #{fileId}
        """)
   List<NoticeBoardFile> selectNamesByFileId(Integer fileId);

   @Delete("""
        DELETE FROM noticeBoardFile
        WHERE fileId = #{fileId}
        """)
   int deleteByFileId(Integer fileId);

   @Select("""
        SELECT * 
        FROM noticeBoardFile 
        WHERE id = #{id}
        """)
   NoticeBoardFile selectById(Integer id);

   @Delete("""
        DELETE FROM noticeBoardFile
        WHERE id = #{id}
        """)
   int deleteById(Integer id);

}
