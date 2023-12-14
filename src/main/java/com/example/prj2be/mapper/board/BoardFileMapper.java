package com.example.prj2be.mapper.board;

import com.example.prj2be.domain.board.BoardFile;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BoardFileMapper {

   @Insert("""
      INSERT INTO boardFile (fileId, fileName)
      VALUES (#{fileId}, #{fileName})
      """)

   int insert(Integer fileId, String fileName);

   @Select("""
        SELECT id, fileName
        FROM boardFile
        WHERE fileId = #{fileId}
        """)
   List<BoardFile> selectNamesByFileId(Integer fileId);

   @Delete("""
        DELETE FROM boardFile
        WHERE fileId = #{fileId}
        """)
   int deleteByFileId(Integer fileId);

   @Select("""
        SELECT * 
        FROM boardFile 
        WHERE id = #{id}
        """)
   BoardFile selectById(Integer id);

   @Delete("""
        DELETE FROM boardFile
        WHERE id = #{id}
        """)
   int deleteById(Integer id);

}
