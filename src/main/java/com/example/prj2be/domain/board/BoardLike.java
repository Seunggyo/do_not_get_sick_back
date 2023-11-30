package com.example.prj2be.domain.board;

import lombok.Data;

@Data
public class BoardLike {

   private Integer id;
   private Integer boardId;
   private String memberId;
}
