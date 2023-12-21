package com.example.prj2be.mapper.drug;

import com.example.prj2be.domain.drug.Like;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

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

    @Select("""
            SELECT COUNT(id) FROM drugLike
            WHERE drugId = #{drugId}
            """)
    int countByDrugId(Integer drugId);

    @Select("""
            SELECT *
            FROM drugLike
            WHERE drugId = #{drugId}
            AND memberId = #{memberId}
            """)
    Like selectByDrugIdAndMemberId(Integer drugId, String memberId);

    @Delete("""
        delete from drugLike
        where drugId = #{drugId}
""")
    void deleteByDrugId(Integer drugId);

    @Delete("""
        delete from drugLike
        where memberId = #{memberId}
            """)
    void deleteByMemberId(String memberId);
}
