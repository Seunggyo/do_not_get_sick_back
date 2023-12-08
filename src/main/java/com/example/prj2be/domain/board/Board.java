package com.example.prj2be.domain.board;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Board {

   private Integer id;
   private String title;
   private String content;
   private String writer;
   private String nickName;
   private String category;
   private LocalDate inserted;
   private Integer increaseHit;
   private Integer countComment;
   private Integer countLike;

//   public String getAgo() {
//      return AppUtil.getAgo(inserted, LocalDateTime.now());
//   }

}
