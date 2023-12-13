package com.example.prj2be.mapper.cs_qa;

import com.example.prj2be.domain.board.BoardFile;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface NoticeQaBoardFileMapper {

   @Insert("""
      INSERT INTO noticeQaBoardFile (fileId, fileName)
      VALUES (#{fileId}, #{fileName})
      """)

   int insert(Integer fileId, String fileName);

   @Select("""
        SELECT id, fileName
        FROM noticeQaBoardFile
        WHERE fileId = #{fileId}
        """)
   List<com.example.prj2be.domain.cs_qa.NoticeQaBoardFile> selectNamesByFileId(Integer fileId);

   @Delete("""
        DELETE FROM noticeQaBoardFile
        WHERE fileId = #{fileId}
        """)
   int deleteByFileId(Integer fileId);

   @Select("""
        SELECT * 
        FROM noticeQaBoardFile 
        WHERE id = #{id}
        """)
   BoardFile selectById(Integer id);

   @Delete("""
        DELETE FROM noticeQaBoardFile
        WHERE id = #{id}
        """)
   int deleteById(Integer id);
}
