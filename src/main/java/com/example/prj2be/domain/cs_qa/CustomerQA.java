package com.example.prj2be.domain.cs_qa;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CustomerQA {

   private Integer id;
   private String qaTitle;
   private String qaContent;
   private String qaWriter;
   private String qaCategory;
   private LocalDateTime inserted;

}
