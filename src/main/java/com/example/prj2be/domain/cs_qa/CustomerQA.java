package com.example.prj2be.domain.cs_qa;

import com.example.prj2be.utill.AppUtil;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CustomerQA {

   private Integer id;
   private String qaTitle;
   private String qaContent;
   private String qaWriter;
   private String qaCategory;
   private String category;
   private Integer countComment;
   private LocalDateTime inserted;
   public String getAgo() {
      return AppUtil.getAgo(inserted);
   }

}
