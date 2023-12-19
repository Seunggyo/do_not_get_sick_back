package com.example.prj2be.domain.cs_qa;

import com.example.prj2be.domain.board.BoardFile;
import com.example.prj2be.utill.AppUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class CustomerService {

   private Integer id;
   private String csCategory;
   private String csTitle;
   private String csContent;
   private String csWriter;
   private String csNickName;
   private LocalDateTime inserted;
   private Integer increaseHit;
   private Integer countFile;

   private List<NoticeBoardFile> files;

   public String getAgo() {
      return AppUtil.getAgo(inserted);
   }

}
