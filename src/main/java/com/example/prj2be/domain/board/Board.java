package com.example.prj2be.domain.board;

import com.example.prj2be.utill.AppUtil;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class Board {

   private Integer id;
   private String title;
   private String content;
   private String writer;
   private String nickName;
   private String category;
   private LocalDateTime inserted;
   private Integer increaseHit;
   private Integer countComment;
   private Integer countLike;
   private Integer countFile;

   private List<BoardFile> files;

   public String getAgo() {
      return AppUtil.getAgo(inserted);
   }

}
