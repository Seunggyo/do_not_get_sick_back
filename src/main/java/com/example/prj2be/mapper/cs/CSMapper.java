package com.example.prj2be.mapper.cs;

import com.example.prj2be.domain.cs.CustomerService;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CSMapper {

   @Insert("""
      INSERT INTO customerService (csTitle, csContent, csWriter)
      VALUES (#{csTitle}, #{csContent}, #{csWriter})
      """)
   int insert(CustomerService cs);

   @Select("""
      SELECT id, csTitle, csWriter, instered
      FROM customerService
      ORDER BY id DESC 
      """)
   List<CustomerService> selectAll();


   @Select("""
      SELECT *
      FROM customerService
      WHERE id= #{id}
      """)
   CustomerService selectById(Integer id);
}
