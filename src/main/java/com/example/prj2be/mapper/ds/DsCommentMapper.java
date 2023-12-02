package com.example.prj2be.mapper.ds;

import com.example.prj2be.domain.ds.DsComment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DsCommentMapper {

    @Insert("""
            INSERT INTO businesscomment (businessId, memberId, comment)
            VALUES (#{businessId}, #{memberId}, #{comment})
            """)
    int insert(DsComment comment);

    @Select("""
            SELECT bc.id, bc.businessId, bc.memberId, bc.comment, bc.inserted, m.nickName `memberNickName`
            FROM businesscomment bc
                JOIN member m
                    ON bc.memberId = m.id
            WHERE businessId = #{businessId}
            ORDER By id DESC ;
            """)
    List<DsComment> selectByBusinessId(Integer businessId);
}
