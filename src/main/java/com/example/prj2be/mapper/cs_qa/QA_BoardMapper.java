package com.example.prj2be.mapper.cs_qa;

import com.example.prj2be.domain.cs_qa.QA;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QA_BoardMapper {

   @Insert("""
      INSERT INTO qa (qa_Title, qa_Content, qa_Writer, qa_Category)
      VALUES (#{qa_Title}, #{qa_Content}, #{qa_Writer}, #{qa_Category})
      """)
   int insert(QA qa);

   int countAll(String s);

   Object selectAll(int from, String s);

   QA selectById(Integer id);
}
