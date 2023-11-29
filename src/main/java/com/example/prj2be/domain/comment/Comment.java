package com.example.prj2be.domain.comment;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Comment {

   private Integer id;
   private Integer boardId;
   private String memberId;
   private String comment;
   private LocalDate inserted;
}


