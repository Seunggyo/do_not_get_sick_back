package com.example.prj2be.controller.cs;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CS {

   private Integer id;
   private String csTitle;
   private String csContent;
   private String csWriter;
   private LocalDateTime instered;
}
