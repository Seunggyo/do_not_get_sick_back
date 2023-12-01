package com.example.prj2be.mapper.drug;


import com.example.prj2be.domain.drug.DrugComment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
    void deleteById(Integer id);
}
