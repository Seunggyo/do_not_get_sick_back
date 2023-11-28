package com.example.prj2be.mapper.ds;

import com.example.prj2be.domain.ds.DsLike;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DsLikeMapper {
    @Delete("""
            DELETE FROM businesslike
            WHERE memberId = #{memberId}
            AND businessId = #{businessId}
            """)
    int delete(DsLike like);

    @Insert("""
            INSERT INTO businesslike (memberId, businessId)
            VALUES (#{memberId}, #{businessId})
            """)
    int insert(DsLike like);

    @Select("""
            SELECT COUNT(id)
            FROM businesslike
            WHERE memberId = #{memberId}
            """)
    int countByMemberId(String memberId);
}
