package com.example.prj2be.domain.cs;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CustomerService {

   private Integer id;
   private String csCategory;
   private String csTitle;
   private String csContent;
   private String csWriter;
   private LocalDateTime inserted;
   private Integer csHit;
}
