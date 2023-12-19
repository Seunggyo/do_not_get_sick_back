package com.example.prj2be.domain.hs;

import com.example.prj2be.utill.AppUtil;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class HsComment {

    private Integer id;
    private Integer businessId;
    private String memberId;
    private String comment;
    private LocalDateTime inserted;
    private String memberNickName;

    public String getAgo() {
        return AppUtil.getAgo(inserted);
    }
}
