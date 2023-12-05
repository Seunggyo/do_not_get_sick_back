package com.example.prj2be.mapper.cs;

import com.example.prj2be.domain.cs.CustomerService;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CSMapper {

   @Insert("""
      INSERT INTO customerService (csTitle, csCategory, csContent, csWriter)
      VALUES (#{csTitle}, #{csCategory}, #{csContent}, #{csWriter})
      """)
   int insert(CustomerService cs);

   @Select("""
      SELECT id, csTitle, csCategory, csWriter, inserted, csHit
      FROM customerService
      ORDER BY id DESC 
      LIMIT #{from}, 10
      """)
   List<CustomerService> selectAll(Integer from, String s);


   @Select("""
      SELECT id, csTitle, csCategory, csWriter, inserted, csHit
      FROM customerService
      ORDER BY csHit ASC
      """)
   Map<String, Object> selectAllOrderByHitAsc();
   @Select("""
      SELECT id, csTitle, csCategory, csWriter, inserted, csHit
      FROM customerService
      ORDER BY csHit DESC
      """)
   Map<String, Object> selectAllOrderByHitDesc();

   @Select("""
      SELECT id, csTitle, csCategory, csWriter, inserted, csHit
      FROM customerService
      ORDER BY csTitle DESC
      """)
   Map<String, Object> selectAllOrderByTitleDesc();

   @Select("""
      SELECT id, csTitle, csCategory, csWriter, inserted, csHit
      FROM customerService
      ORDER BY csTitle ASC
      """)
   Map<String, Object> selectAllOrderByTitleAsc();

   @Select("""
      SELECT *
      FROM customerService
      WHERE id= #{id}
      """)
   CustomerService selectById(Integer id);

   @Delete("""
      DELETE FROM customerService 
      WHERE id=#{id}
      """
   )
   int deleteById(Integer id);

   @Update("""
      UPDATE customerService
      SET csTitle = #{csTitle},
         csContent = #{csContent},
         csWriter = #{csWriter},
         csCategory = #{csCategory}
      WHERE id = #{id}
      """)
   int update(CustomerService cs);


   @Delete("""
        DELETE FROM board
        WHERE csWriter = #{csWriter}
        """)

   int deleteByWriter(String writer);

   @Select("""
      SELECT id
      FROM customerService
      WHERE writer = #{id}
      """)

   List<Integer> selectIdListByMemberId(String writer);

   @Update("""
      UPDATE customerService
      SET csHit = csHit + 1
      WHERE id = #{id}
      """)
   void increaseHit(int id);

   @Select("""
        SELECT COUNT(*) FROM board
        WHERE title LIKE #{keyword}
           OR content LIKE #{keyword}
        """)
   int countAll(String keyword);
}
