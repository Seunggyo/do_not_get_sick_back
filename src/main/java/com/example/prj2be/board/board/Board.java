package com.example.prj2be.board.board;

import java.time.LocalDateTime;
import lombok.Data;
import org.apache.ibatis.annotations.Insert;

@Data
public class Board {

   private Integer id;
   private String title;
   private String content;
   private String writer;
   private String category;
   private LocalDateTime inserted;

}
