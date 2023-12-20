package com.example.prj2be.domain.cs_qa;

import com.example.prj2be.domain.board.BoardFile;
import com.example.prj2be.utill.AppUtil;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class CustomerQA {

   private Integer id;
   private String qaTitle;
   private String qaContent;
   private String qaWriter;
   private String qaNickName;
   private String qaCategory;
   private String category;
   private LocalDateTime inserted;
   private Integer countComment;
   private Integer countFile;

   private List<NoticeQaBoardFile> files;
   public String getAgo() {
      return AppUtil.getAgo(inserted);
   }

}
