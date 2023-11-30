package com.example.prj2be.domain.board;

import java.time.LocalDate;
import lombok.Data;

@Data
public class BoardComment {

   private Integer id;
   private Integer boardId;
   private String memberId;
   private String memberNickName;
   private String comment;
   private LocalDate inserted;
}


