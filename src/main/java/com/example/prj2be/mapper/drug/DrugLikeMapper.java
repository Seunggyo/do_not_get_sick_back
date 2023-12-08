package com.example.prj2be.mapper.drug;

import com.example.prj2be.domain.drug.Like;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DrugLikeMapper {

    @Delete("""
            DELETE FROM drugLike
            WHERE drugId = #{drugId}
            AND memberid = #{memberId}
            """)
    int delete(Like like);

    @Insert("""
            INSERT INTO drugLike(drugId, memberId)
            VALUES (#{drugId}, #{memberId})
            """)
    int insert(Like like);
}
