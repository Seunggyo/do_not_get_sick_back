package com.example.prj2be.mapper.cs_qa;

import com.example.prj2be.domain.cs_qa.CustomerQA;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface QAMapper {

   @Insert("""
      INSERT INTO customerqa (qaTitle, qaContent, qaWriter, qaCategory)
      VALUES (#{qaTitle}, #{qaContent}, #{qaWriter}, #{qaCategory})
      """)
   @Options(useGeneratedKeys = true, keyProperty = "id")
   int insert(CustomerQA qa);

   @Select("""
        SELECT q.id,
               q.qaTitle,
               q.qaContent,
               q.qaWriter,
               q.qaCategory,
               m.nickName,
               q.inserted,
               COUNT(DISTINCT c.id) countComment,
               COUNT(DISTINCT f.id) countFile
        FROM customerqa q JOIN member m ON q.qaWriter = m.id
        LEFT JOIN noticeQaBoardFile f ON q.id = f.fileId
        LEFT JOIN boardComment c 
        ON q.id = c.boardId
        and c.category = "qa"
        WHERE (q.qaContent LIKE #{keyword}
           OR q.qaTitle LIKE #{keyword})
           AND q.qaCategory Like #{filter}
           AND q.qaWriter = #{qaWriter}
        GROUP BY q.id
        ORDER BY q.id DESC
        LIMIT #{from}, 10
        """)
   List<CustomerQA> selectAll(Integer from, String keyword, String filter, String qaWriter);

   @Select("""
        SELECT q.id,
               q.qaTitle,
               q.qaCategory,
               q.qaContent,
               q.qaWriter, 
               m.NickName,
               q.inserted
        FROM customerqa q JOIN member m ON q.qaWriter = m.id
        WHERE q.id = #{id}
        """)
   CustomerQA selectById(Integer id);

   @Delete("""
        DELETE FROM customerqa
        WHERE id = #{id}
        """)
   int deleteById(Integer id);

   @Update("""
      UPDATE customerqa
      SET qaTitle = #{qaTitle},
          qaContent = #{qaContent},
          qaWriter = #{qaWriter},
          qaCategory = #{qaCategory}
      WHERE id = #{id}
      """)
   int update(CustomerQA qa);


//   @Delete("""
//        DELETE FROM customerqa
//        WHERE qaWriter = #{qaWriter}
//        """)
//
//   int deleteByWriter(String qaWriter);
//
//   @Select("""
//        SELECT id
//        FROM customerqa
//        WHERE qaWriter = #{id}
//        """)
//   List<Integer> selectIdListByMemberId(String qaWriter);

   @Select("""
        SELECT COUNT(*) FROM customerqa
        WHERE qaTitle LIKE #{keyword}
           OR qaCategory LIKE #{keyword}
        """)
   int countAll(String keyword, String filter, String qaWriter);

   @Select("""
        SELECT q.id,
               q.qaTitle,
               q.qaContent,
               q.qaWriter,
               q.qaCategory,
               m.nickName,
               q.inserted,
               COUNT(DISTINCT c.id) countComment,
               COUNT(DISTINCT f.id) countFile
        FROM customerqa q JOIN member m ON q.qaWriter = m.id
        LEFT JOIN noticeQaBoardFile f ON q.id = f.fileId
        LEFT JOIN boardComment c 
        ON q.id = c.boardId
        and c.category = "qa"
        WHERE (q.qaContent LIKE #{keyword}
           OR q.qaTitle LIKE #{keyword})
           AND q.qaCategory Like #{filter}
        GROUP BY q.id
        ORDER BY q.id DESC
        LIMIT #{from}, 10
        """)
   List<CustomerQA> adminSelectAll(int from, String keyword, String filter);

   @Select("""
        SELECT COUNT(*) FROM customerqa
        WHERE qaTitle LIKE #{keyword}
           OR qaCategory LIKE #{keyword}
        """)
   int adminCountAll(String keyword, String filter);
}