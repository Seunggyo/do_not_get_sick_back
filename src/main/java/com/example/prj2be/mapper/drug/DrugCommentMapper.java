package com.example.prj2be.mapper.drug;


import com.example.prj2be.domain.drug.DrugComment;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface DrugCommentMapper {

    @Insert("""
            INSERT INTO drugComment(drugId, comment, memberId)
            VALUES (#{drugId}, #{comment}, #{memberId})
            """)
    int insert(DrugComment drugComment);

    @Select("""
            SELECT *
            FROM drugComment
            WHERE drugId = #{drugId}
            ORDER BY id DESC
            """)
    List<DrugComment> selectByDrugId(Integer drugId);

    @Delete("""
            DELETE FROM drugComment
            WHERE id = #{id}
            """)
    int deleteById(Integer id);

    @Select("""
            SELECT * FROM drugComment
            WHERE id = #{id}
            """)
    DrugComment selectById(Integer id);

    @Update("""
            UPDATE drugComment
            SET comment = #{comment}
            WHERE id = #{id}
            """)
    int update(DrugComment comment);
}
