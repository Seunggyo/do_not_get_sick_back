package com.example.prj2be.board.board;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Board {

   private Integer id;
   private String title;
   private String content;
   private String writer;
   private String category;
   private LocalDateTime inserted;

}
