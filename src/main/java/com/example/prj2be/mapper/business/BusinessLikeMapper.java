package com.example.prj2be.mapper.business;

import com.example.prj2be.domain.business.BusinessLike;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BusinessLikeMapper {
    @Delete("""
            DELETE FROM businesslike
            WHERE memberId = #{memberId}
            AND businessId = #{businessId}
            """)
    int delete(BusinessLike like);

    @Insert("""
            INSERT INTO businesslike (memberId, businessId)
            VALUES (#{memberId}, #{businessId})
            """)
    int insert(BusinessLike like);

    @Select("""
            SELECT COUNT(id)
            FROM businesslike
            WHERE businessId = #{businessId}
            """)
    int countByMemberId(Integer dsId);

    @Select("""
            SELECT * FROM businesslike
            WHERE businessId = #{businessId}
              AND memberId = #{memberId}
            """)
    BusinessLike selectByDsIdAndMemberId(Integer dsId, String memberId);
}
