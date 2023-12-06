package com.example.prj2be.domain.cs_qa;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class QA {

   private Integer qa_Id;
   private String qa_Title;
   private String qa_Content;
   private String qa_Writer;
   private String qa_NickName;
   private String qa_Category;
   private LocalDateTime inserted;

}
