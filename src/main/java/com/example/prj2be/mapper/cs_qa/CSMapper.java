package com.example.prj2be.mapper.cs_qa;

import com.example.prj2be.domain.cs_qa.CustomerService;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CSMapper {

   @Insert("""
      INSERT INTO customerService (csTitle, csCategory, csContent, csWriter)
      VALUES (#{csTitle}, #{csCategory}, #{csContent}, #{csWriter})
      """)
   @Options(useGeneratedKeys = true, keyProperty = "id")
   int insert(CustomerService cs);

   @Select("""
      SELECT   c.id, 
               c.csTitle, 
               c.csCategory, 
               c.csWriter,
               m.nickName, 
               c.inserted, 
               c.increaseHit,
               COUNT(DISTINCT f.id) countFile
      FROM customerService c 
               JOIN member m 
                  ON c.csWriter = m.id
               LEFT JOIN noticeBoardFile f 
                  ON c.id = f.fileId
      WHERE   (c.csTitle      LIKE #{keyword}
            or c.csCategory   LIKE #{keyword}
            or m.nickName     LIKE #{keyword})
            AND c.csCategory Like #{filter}
      Group BY c.id
      ORDER BY c.id DESC 
      LIMIT #{from}, 10
   """)

   List<CustomerService> selectAll(Integer from, String keyword, String filter);

   @Select("""
      SELECT   c.id,
               c.csTitle, 
               c.csContent, 
               c.csWriter,
               m.nickName,
               c.csCategory,
               c.inserted
      FROM customerService c JOIN member m ON c.csWriter = m.id
      WHERE c.id = #{id}
   """)
   CustomerService selectById(Integer id);

   @Delete("""
      DELETE FROM customerService 
      WHERE id = #{id}
   """)
   int deleteById(Integer id);

   @Update("""
      UPDATE customerService
      SET   csTitle = #{csTitle},
            csContent = #{csContent},
            csCategory = #{csCategory}
      WHERE id = #{id}
      """)
   int update(CustomerService cs);


   @Select("""
      SELECT COUNT(*) FROM customerService 
      WHERE (csTitle LIKE #{keyword}
         OR csCategory LIKE #{keyword})
         AND csCategory Like #{filter}
      """)
   int countAll(String keyword, String filter);

   @Update("""
      UPDATE customerService
      SET   increaseHit = increaseHit + 1
      WHERE id = #{id}
      """)
   void increaseHit(int id);

   @Delete("""
      delete from customerService
      where csWriter = #{memberId}
""")
    void deleteByMemberId(String memberId);

   @Select("""
      select * from customerService
      where csWriter = #{memberId}
""")
   List<CustomerService> selectByMemberId(String memberId);
}

